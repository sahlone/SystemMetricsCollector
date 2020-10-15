package com.sahlone.smc.app.ft;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MetricsPublisherTest {

  static KafkaConsumer kafkaConsumer;

  @BeforeAll
  public static void prepareConsumer() {
    Map<String, Object> configMap = new HashMap();
    configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configMap.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
    configMap.put(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringDeserializer");
    configMap.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringDeserializer");
    kafkaConsumer = new KafkaConsumer(configMap);
    kafkaConsumer.subscribe(List.of("system-metrics"));
  }

  @AfterAll
  public static void closeConsumer() {
    kafkaConsumer.close();
  }

  @Test
  public void shouldPublishTest() {
    List<ConsumerRecord<String, String>> metrics = new ArrayList<>();
    ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(Long.MAX_VALUE));
    records.forEach(
        record -> {
          metrics.add(record);
        });
    assertEquals(false, metrics.isEmpty(), "metrics should have been published to the kafka");
  }
}
