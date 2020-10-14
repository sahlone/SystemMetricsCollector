package com.sahlone.app.smc.models;

public class KafkaConsumerConfig {
  private String bootstrapServers;
  private String groupId;
  private String topic;
  private AutoOffsetReset offsetReset;
  private Long sessionTimeoutMs;
  private Long heartbeatIntervalMs;
  private Long maxPollIntervalMs;
  private Long maxPollRecords;
  private Boolean autoCommit;
  private Integer pollsPerCommit;

  public KafkaConsumerConfig() {
    super();
  }

  public KafkaConsumerConfig(
      String bootstrapServers,
      String groupId,
      String topic,
      AutoOffsetReset offsetReset,
      Long sessionTimeoutMs,
      Long heartbeatIntervalMs,
      Long maxPollIntervalMs,
      Long maxPollRecords,
      Boolean autoCommit,
      Integer pollsPerCommit) {
    this.bootstrapServers = bootstrapServers;
    this.groupId = groupId;
    this.topic = topic;
    this.offsetReset = offsetReset;
    this.sessionTimeoutMs = sessionTimeoutMs;
    this.heartbeatIntervalMs = heartbeatIntervalMs;
    this.maxPollIntervalMs = maxPollIntervalMs;
    this.maxPollRecords = maxPollRecords;
    this.autoCommit = autoCommit;
    this.pollsPerCommit = pollsPerCommit;
  }

  public String getBootstrapServers() {
    return bootstrapServers;
  }

  public void setBootstrapServers(String bootstrapServers) {
    this.bootstrapServers = bootstrapServers;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public AutoOffsetReset getOffsetReset() {
    return offsetReset;
  }

  public void setOffsetReset(AutoOffsetReset offsetReset) {
    this.offsetReset = offsetReset;
  }

  public Long getSessionTimeoutMs() {
    return sessionTimeoutMs;
  }

  public void setSessionTimeoutMs(Long sessionTimeoutMs) {
    this.sessionTimeoutMs = sessionTimeoutMs;
  }

  public Long getHeartbeatIntervalMs() {
    return heartbeatIntervalMs;
  }

  public void setHeartbeatIntervalMs(Long heartbeatIntervalMs) {
    this.heartbeatIntervalMs = heartbeatIntervalMs;
  }

  public Long getMaxPollIntervalMs() {
    return maxPollIntervalMs;
  }

  public void setMaxPollIntervalMs(Long maxPollIntervalMs) {
    this.maxPollIntervalMs = maxPollIntervalMs;
  }

  public Long getMaxPollRecords() {
    return maxPollRecords;
  }

  public void setMaxPollRecords(Long maxPollRecords) {
    this.maxPollRecords = maxPollRecords;
  }

  public Boolean getAutoCommit() {
    return autoCommit;
  }

  public void setAutoCommit(Boolean autoCommit) {
    this.autoCommit = autoCommit;
  }

  public Integer getPollsPerCommit() {
    return pollsPerCommit;
  }

  public void setPollsPerCommit(Integer pollsPerCommit) {
    this.pollsPerCommit = pollsPerCommit;
  }
}
