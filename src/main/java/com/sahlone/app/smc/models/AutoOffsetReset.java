package com.sahlone.app.smc.models;

public enum AutoOffsetReset {
  Latest,
  Earliest,
  None;

  public String show() {
    String result = "None";
    switch (this) {
      case Latest:
        result = "latest";
        break;
      case Earliest:
        result = "earliest";
        break;
      default:
        result = "None";
    }
    return result;
  }
}
