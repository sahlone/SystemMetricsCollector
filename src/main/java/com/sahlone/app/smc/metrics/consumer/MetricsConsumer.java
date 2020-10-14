package com.sahlone.app.smc.metrics.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahlone.app.smc.logging.Argument;
import com.sahlone.app.smc.logging.ContextualLogger;
import com.sahlone.app.smc.logging.TracingContext;
import com.sahlone.app.smc.models.KafkaConsumerConfig;
import com.sahlone.app.smc.models.MetricsHandlerResult;
import com.sahlone.app.smc.models.SystemMetrics;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import kafka.common.KafkaException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class MetricsConsumer implements Runnable {

  private ContextualLogger logger = new ContextualLogger(MetricsConsumer.class);
  private KafkaConsumerConfig consumerConfig;
  private MetricsHandler handler;
  private ObjectMapper mapper;
  private AtomicBoolean shutdown = new AtomicBoolean(false);
  private CountDownLatch started = new CountDownLatch(1);
  private CountDownLatch shutdownLatch = new CountDownLatch(1);
  private Map<String, Object> configMap = new HashMap();
  private Thread thread;
  private KafkaConsumer consumer;

  private TracingContext tracingContext = TracingContext.of();

  public MetricsConsumer(
      KafkaConsumerConfig consumerConfig, MetricsHandler handler, ObjectMapper mapper) {
    this.consumerConfig = consumerConfig;
    this.handler = handler;
    this.mapper = mapper;
    configMap.put(
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerConfig.getAutoCommit().toString());
    configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerConfig.getOffsetReset().show());
    configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerConfig.getBootstrapServers());
    configMap.put(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
    configMap.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfig.getGroupId());
    configMap.put(
        ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerConfig.getSessionTimeoutMs().toString());
    configMap.put(
        ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerConfig.getMaxPollRecords().toString());
    configMap.put(
        ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG,
        consumerConfig.getHeartbeatIntervalMs().toString());
    configMap.put(
        ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,
        consumerConfig.getMaxPollIntervalMs().toString());
    configMap.put(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringDeserializer");
    configMap.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringDeserializer");
    this.thread =
        new Thread(
            new ThreadGroup("kafka-consumer"),
            this,
            "kafka-consumer-" + consumerConfig.getGroupId() + Math.random());
    this.consumer = new KafkaConsumer<String, String>(configMap);
  }

  private MetricsConsumer() {
    super();
  }

  public void start() throws InterruptedException {
    logger.debug(
        tracingContext, " Starting metrics consumer", Argument.of("config", consumerConfig));
    thread.start();
    if (!started.await(consumerConfig.getMaxPollIntervalMs(), TimeUnit.MILLISECONDS)) {
      KafkaException exception = new KafkaException("Error staring metrics consumer");
      logger.error(
          tracingContext, "Error starting metrics consumer", Argument.of("config", consumerConfig));
      throw exception;
    }
  }

  public void stop() {
    try {
      shutdown.set(true);
      shutdownLatch.await(consumerConfig.getMaxPollIntervalMs(), TimeUnit.MILLISECONDS);
      logger.debug(tracingContext, "Metrics consumer stopped successfully");
    } catch (InterruptedException err) {
      logger.errorWithCause(tracingContext, err, "Failed to stop metrics consumer");
    }
  }

  @Override
  public void run() {

    try {
      consumer.subscribe(List.of(consumerConfig.getTopic()));
      started.countDown();
      logger.debug(
          tracingContext, "Metrics consumer started", Argument.of("config", consumerConfig));

      while (!shutdown.get()) {
        try {
          for (int index = 1; index <= consumerConfig.getPollsPerCommit(); index++) {
            ConsumerRecords<String, String> records =
                consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
            records.forEach(
                record -> {
                  TracingContext tracingContext =
                      record.key() == null ? TracingContext.of() : new TracingContext(record.key());
                  try {
                    SystemMetrics metrics = mapper.readValue(record.value(), SystemMetrics.class);
                    MetricsHandlerResult result = handler.handle(tracingContext, metrics);
                    if (!result.success) {
                      logger.errorWithCause(
                          tracingContext,
                          result.error,
                          "Kafka consumer failed to handle records",
                          Argument.of("record", record.value()));
                    } else {
                      logger.debug(
                          tracingContext,
                          "Successfully consumed metrics",
                          Argument.of("metrics", metrics));
                    }
                  } catch (JsonProcessingException e) {
                    logger.errorWithCause(
                        tracingContext,
                        e,
                        "Error deserialize system metrics",
                        Argument.of("record", record.value()));
                  }
                });
          }
          consumer.commitSync();
        } catch (Exception err) {
          logger.errorWithCause(
              tracingContext,
              err,
              "Error in kafka consumer while consuming message",
              Argument.of("config", consumerConfig));
        }
      }
    } catch (Exception err) {
      logger.errorWithCause(
          tracingContext, err, "Error in kafka consumer", Argument.of("config", consumerConfig));
    } finally {
      consumer.close();
      shutdownLatch.countDown();
    }
  }
}
