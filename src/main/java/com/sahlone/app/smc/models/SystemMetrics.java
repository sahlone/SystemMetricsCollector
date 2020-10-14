package com.sahlone.app.smc.models;

import java.time.Instant;
import java.util.UUID;

public class SystemMetrics {

  private UUID traceId;
  private SystemMetricsLabel label;
  private Double value;
  private Instant sampleTime;

  public SystemMetrics() {
    super();
  }

  public SystemMetrics(SystemMetricsLabel label, Double value) {
    this.traceId = UUID.randomUUID();
    this.label = label;
    this.value = value;
    this.sampleTime = Instant.now();
  }

  public UUID getTraceId() {
    return traceId;
  }

  public void setTraceId(UUID traceId) {
    this.traceId = traceId;
  }

  public SystemMetricsLabel getLabel() {
    return label;
  }

  public void setLabel(SystemMetricsLabel label) {
    this.label = label;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public Instant getSampleTime() {
    return sampleTime;
  }

  public void setSampleTime(Instant sampleTime) {
    this.sampleTime = sampleTime;
  }
}
