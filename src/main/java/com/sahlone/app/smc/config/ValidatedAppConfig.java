package com.sahlone.app.smc.config;

import com.sahlone.app.smc.logging.Argument;
import com.sahlone.app.smc.logging.ContextualLogger;
import com.sahlone.app.smc.logging.TracingContext;
import com.sahlone.app.smc.models.KafkaConsumerConfig;
import com.sahlone.app.smc.models.KafkaProducerConfig;
import com.sahlone.app.smc.repository.models.DatabaseConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class ValidatedAppConfig {

  private static ContextualLogger logger = new ContextualLogger(ValidatedAppConfig.class);
  private AppConfig appConfig;

  public static AppConfig instance(Optional<String> resource) {
    Config config;
    if (resource.isEmpty()) {
      config = ConfigFactory.load().getConfig("systemMetrics");
    } else {
      config = ConfigFactory.load(resource.get()).getConfig("systemMetrics");
    }
    Duration metricsPublishDelay = config.getDuration("metricsPublishDelay");
    Boolean enableProducer = config.getBoolean("enableProducer");
    Boolean enableConsumer = config.getBoolean("enableConsumer");
    DatabaseConfig databaseConfig =
        ConfigBeanFactory.create(config.getConfig("dbConfig"), DatabaseConfig.class);
    KafkaProducerConfig kafkaProducerConfig =
        ConfigBeanFactory.create(config.getConfig("kafkaProducer"), KafkaProducerConfig.class);
    KafkaConsumerConfig kafkaConsumerConfig =
        ConfigBeanFactory.create(config.getConfig("kafkaConsumer"), KafkaConsumerConfig.class);
    if (enableConsumer || enableProducer) {
      return new AppConfig(
          metricsPublishDelay,
          enableProducer,
          enableConsumer,
          databaseConfig,
          kafkaProducerConfig,
          kafkaConsumerConfig);
    } else {
      logger.error(TracingContext.of(), "", Argument.of("config", config));
      throw new ConfigException.ValidationFailed(
          List.of(
              new ConfigException.ValidationProblem(
                  "systemMetrics",
                  null,
                  "Either enableProducer or enableConsumer should be set to true. Please check the config")));
    }
  }
}
