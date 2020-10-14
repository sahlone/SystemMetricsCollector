package com.sahlone.app.smc.metrics.consumer;

import com.sahlone.app.smc.logging.TracingContext;
import com.sahlone.app.smc.models.MetricsHandlerResult;
import com.sahlone.app.smc.models.SystemMetrics;

public interface MetricsHandler {

  MetricsHandlerResult handle(TracingContext tracingContext, SystemMetrics metrics);
}
