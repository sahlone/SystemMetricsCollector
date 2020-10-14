package com.sahlone.app.smc.logging;

public final class Argument {
  String key;
  Object value;

  private Argument() {
    super();
  }

  public Argument(String key, Object value) {
    this.key = key;
    this.value = value;
  }

  public static Argument of(String key, Object value) {
    return new Argument(key, value);
  }
}
