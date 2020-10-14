package com.sahlone.app.smc.logging;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class ContextualLogger<T> {
  private Logger delegate;

  public ContextualLogger(Class clazz) {
    this.delegate = LoggerFactory.getLogger(clazz);
  }

  private Marker generateMarker(TracingContext context, Map<String, Object> markers) {
    return Markers.appendEntries(markers);
  }

  public void debug(TracingContext context, String message, Argument... markers) {
    if (delegate.isDebugEnabled()) {
      Map<String, Object> mks =
          Arrays.stream(markers)
              .collect(Collectors.toMap(argument -> argument.key, argument -> argument.value));
      delegate.debug(generateMarker(context, mks), message);
    }
  }

  public void debugWithCause(
      TracingContext context, Throwable error, String message, Argument... markers) {
    if (delegate.isDebugEnabled()) {
      Map<String, Object> mks =
          Arrays.stream(markers)
              .collect(Collectors.toMap(argument -> argument.key, argument -> argument.value));
      delegate.debug(generateMarker(context, mks), message, error);
    }
  }

  public void error(TracingContext context, String message, Argument... markers) {
    if (delegate.isErrorEnabled()) {
      Map<String, Object> mks =
          Arrays.stream(markers)
              .collect(Collectors.toMap(argument -> argument.key, argument -> argument.value));
      delegate.error(generateMarker(context, mks), message);
    }
  }

  public void errorWithCause(
      TracingContext context, Throwable error, String message, Argument... markers) {
    if (delegate.isErrorEnabled()) {
      Map<String, Object> mks =
          Arrays.stream(markers)
              .collect(Collectors.toMap(argument -> argument.key, argument -> argument.value));
      delegate.error(generateMarker(context, mks), message, error);
    }
  }
}
