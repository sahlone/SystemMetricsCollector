package com.sahlone.app.smc.models;

public class MetricsHandlerResult {
  public final Boolean success;
  public final Throwable error;

  public MetricsHandlerResult(Boolean success, Throwable error) {
    this.success = success;
    this.error = error;
  }
}
