package com.sahlone.app.smc.models;

public class KafkaProducerConfig {

  private String bootstrapServers;
  private String clientId;
  private String topic;
  private Boolean idempotence;
  private Long lingerMs;

  public KafkaProducerConfig() {
    super();
  }

  public KafkaProducerConfig(
      String bootstrapServers, String clientId, String topic, Boolean idempotence, Long lingerMs) {
    this.bootstrapServers = bootstrapServers;
    this.clientId = clientId;
    this.topic = topic;
    this.idempotence = idempotence;
    this.lingerMs = lingerMs;
  }

  public String getBootstrapServers() {
    return bootstrapServers;
  }

  public void setBootstrapServers(String bootstrapServers) {
    this.bootstrapServers = bootstrapServers;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public Boolean getIdempotence() {
    return idempotence;
  }

  public void setIdempotence(Boolean idempotence) {
    this.idempotence = idempotence;
  }

  public Long getLingerMs() {
    return lingerMs;
  }

  public void setLingerMs(Long lingerMs) {
    this.lingerMs = lingerMs;
  }
}
