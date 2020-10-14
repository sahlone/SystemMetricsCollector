package com.sahlone.app.smc.config;

import com.sahlone.app.smc.models.KafkaConsumerConfig;
import com.sahlone.app.smc.models.KafkaProducerConfig;
import com.sahlone.app.smc.repository.models.DatabaseConfig;
import java.time.Duration;

public class AppConfig {
  public final Duration metricsPublishDelay;
  public final Boolean enableProducer;
  public final Boolean enableConsumer;
  public final DatabaseConfig databaseConfig;
  public final KafkaProducerConfig kafkaProducerConfig;
  public final KafkaConsumerConfig kafkaConsumerConfig;

  public AppConfig(
      Duration metricsPublishDelay,
      Boolean enableProducer,
      Boolean enableConsumer,
      DatabaseConfig databaseConfig,
      KafkaProducerConfig kafkaProducerConfig,
      KafkaConsumerConfig kafkaConsumerConfig) {
    this.metricsPublishDelay = metricsPublishDelay;
    this.enableProducer = enableProducer;
    this.enableConsumer = enableConsumer;
    this.databaseConfig = databaseConfig;
    this.kafkaProducerConfig = kafkaProducerConfig;
    this.kafkaConsumerConfig = kafkaConsumerConfig;
  }
}
