package com.sahlone.app.smc.logging;

import java.util.UUID;

public class TracingContext {
  public String context;

  public TracingContext() {
    super();
    context = UUID.randomUUID().toString();
  }

  public TracingContext(String context) {
    super();
    this.context = context;
  }

  public static TracingContext of() {
    return new TracingContext();
  }
}
