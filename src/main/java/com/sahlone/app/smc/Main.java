package com.sahlone.app.smc;

import com.sahlone.app.smc.config.AppConfig;
import com.sahlone.app.smc.config.ValidatedAppConfig;
import com.sahlone.app.smc.data.mapper.ObjectMapperFactory;
import com.sahlone.app.smc.logging.ContextualLogger;
import com.sahlone.app.smc.logging.TracingContext;
import com.sahlone.app.smc.metrics.consumer.DatabaseMetricsHandler;
import com.sahlone.app.smc.metrics.consumer.MetricsConsumer;
import com.sahlone.app.smc.metrics.generator.MetricsGenerator;
import com.sahlone.app.smc.metrics.generator.impl.CpuMetricsGenerator;
import com.sahlone.app.smc.metrics.generator.impl.DiskMetricsGenerator;
import com.sahlone.app.smc.metrics.generator.impl.MemoryMetricsGenerator;
import com.sahlone.app.smc.metrics.publisher.MetricsPublisher;
import com.sahlone.app.smc.repository.DataSource;
import com.sahlone.app.smc.repository.DatabaseMigrations;
import com.sahlone.app.smc.repository.MetricsRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.flywaydb.core.api.FlywayException;

public class Main {
  private static ContextualLogger logger = new ContextualLogger(Main.class);

  private static TracingContext tracingContext = TracingContext.of();

  public static void main(String[] args) {
    try {
      logger.debug(tracingContext, "Starting metrics collector app");
      AppConfig appConfig = ValidatedAppConfig.instance(Optional.empty());
      DataSource ds = new DataSource(appConfig.databaseConfig);
      startDatabaseMigrations(ds);
      if (appConfig.enableConsumer) {
        startConsumer(appConfig, ds);
      }
      if (appConfig.enableProducer) {
        startPublisher(appConfig);
      }

    } catch (InterruptedException | FlywayException | IOException err) {
      logger.errorWithCause(tracingContext, err, "Error staring system metrics application");
      System.exit(1);
    }
  }

  private static void startPublisher(AppConfig appConfig) throws IOException, InterruptedException {
    List<MetricsGenerator> metricsGenerators =
        List.of(
            new CpuMetricsGenerator(), new DiskMetricsGenerator(), new MemoryMetricsGenerator());
    MetricsPublisher metricsPublisher =
        new MetricsPublisher(
            metricsGenerators, appConfig.kafkaProducerConfig, ObjectMapperFactory.create());
    while (true) {
      TracingContext tracingContext = TracingContext.of();
      logger.debug(tracingContext, "Starting to publish metrics");
      metricsPublisher.publishMetrics(tracingContext);
      Thread.sleep(appConfig.metricsPublishDelay.toMillis());
    }
  }

  private static void startConsumer(AppConfig appConfig, DataSource ds)
      throws InterruptedException {
    MetricsConsumer metricsConsumer =
        new MetricsConsumer(
            appConfig.kafkaConsumerConfig,
            new DatabaseMetricsHandler(new MetricsRepository(ds)),
            ObjectMapperFactory.create());
    metricsConsumer.start();
  }

  private static void startDatabaseMigrations(DataSource ds) throws FlywayException {
    new DatabaseMigrations("classpath:/db", ds).migrate();
  }
}
