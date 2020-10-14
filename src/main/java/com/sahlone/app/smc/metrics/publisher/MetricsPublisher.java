package com.sahlone.app.smc.metrics.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahlone.app.smc.logging.Argument;
import com.sahlone.app.smc.logging.ContextualLogger;
import com.sahlone.app.smc.logging.TracingContext;
import com.sahlone.app.smc.metrics.generator.MetricsGenerator;
import com.sahlone.app.smc.models.KafkaProducerConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

public class MetricsPublisher {

  private ContextualLogger logger = new ContextualLogger(MetricsPublisher.class);
  private List<MetricsGenerator> metricsGenerators;
  private KafkaProducer<String, String> publisher;
  private Map<String, Object> configMap = new HashMap();
  private KafkaProducerConfig producerConfig;
  private ObjectMapper objectMapper;

  public MetricsPublisher(
      List<MetricsGenerator> metricsGenerators,
      KafkaProducerConfig producerConfig,
      ObjectMapper objectMapper) {
    this.metricsGenerators = metricsGenerators;
    this.producerConfig = producerConfig;
    this.objectMapper = objectMapper;
    configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerConfig.getBootstrapServers());
    configMap.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, producerConfig.getIdempotence());
    configMap.put(ProducerConfig.LINGER_MS_CONFIG, producerConfig.getLingerMs());
    configMap.put(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    configMap.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    publisher = new KafkaProducer<String, String>(configMap);
  }

  public void publishMetrics(TracingContext tracingContext) {
    metricsGenerators.stream()
        .forEach(
            metricsGenerator -> {
              try {
                metricsGenerator.generate().stream()
                    .forEach(
                        metrics -> {
                          try {
                            logger.debug(
                                tracingContext,
                                "Publishing metrics from metrics generator",
                                Argument.of("generator", metricsGenerator.getClass()));
                            String message = objectMapper.writeValueAsString(metrics);
                            publisher.send(
                                new ProducerRecord(
                                    producerConfig.getTopic(),
                                    metrics.getTraceId().toString(),
                                    message));
                            logger.debug(
                                tracingContext,
                                "Metrics published from metrics generator",
                                Argument.of("generator", metricsGenerator.getClass()));
                          } catch (JsonProcessingException err) {
                            logger.errorWithCause(
                                tracingContext,
                                err,
                                "Error serializing system metrics",
                                Argument.of("metrics", metrics));
                          }
                        });
              } catch (IOException err) {
                logger.errorWithCause(
                    tracingContext,
                    err,
                    "Error generating system metrics",
                    Argument.of("metricsGenerator", metricsGenerator.getClass()));
              }
            });
  }
}
