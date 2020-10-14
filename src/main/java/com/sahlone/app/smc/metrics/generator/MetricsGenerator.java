package com.sahlone.app.smc.metrics.generator;

import com.sahlone.app.smc.models.SystemMetrics;
import java.io.IOException;
import java.util.List;

public interface MetricsGenerator {

  public List<SystemMetrics> generate() throws IOException;
}
