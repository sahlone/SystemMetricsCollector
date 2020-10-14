package com.sahlone.smc.app.test.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sahlone.app.smc.config.AppConfig;
import com.sahlone.app.smc.config.ValidatedAppConfig;
import com.typesafe.config.ConfigException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class AppConfigTest {

  @Test
  public void appConfigLoadFailure() {
    assertThrows(
        ConfigException.class,
        () -> ValidatedAppConfig.instance(Optional.of("test.appconfigtest.fail.conf")));
  }

  @Test
  public void loadAppConfigSuccessfully() {
    AppConfig appConfig =
        ValidatedAppConfig.instance(Optional.of("test.appconfigtest.success.conf"));
    assertEquals(true, appConfig.enableProducer, "producer should be true");
    assertEquals(true, appConfig.enableConsumer, "consumer should be enabled");
    assertEquals("kafka:9093", appConfig.kafkaConsumerConfig.getBootstrapServers());
    assertEquals(
        false, appConfig.kafkaConsumerConfig.getAutoCommit(), "auto commit should be true");
    assertEquals(
        "jdbc:postgresql://postgres:5432/metricscollector", appConfig.databaseConfig.getJdbcUrl());
    assertEquals("metricscollector", appConfig.databaseConfig.getUsername());
    assertEquals("kafka:9093", appConfig.kafkaProducerConfig.getBootstrapServers());
    assertEquals("system-metrics-app", appConfig.kafkaProducerConfig.getClientId());
    assertEquals(
        true, appConfig.kafkaProducerConfig.getIdempotence(), "idempotence should be true");
  }
}
