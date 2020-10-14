package com.sahlone.app.smc.metrics.generator.impl;

import com.sahlone.app.smc.metrics.generator.MetricsGenerator;
import com.sahlone.app.smc.models.SystemMetrics;
import com.sahlone.app.smc.models.SystemMetricsLabel;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DiskMetricsGenerator implements MetricsGenerator {

  private File file;

  public DiskMetricsGenerator() throws IOException {
    file = new File("/");
  }

  @Override
  public List<SystemMetrics> generate() throws IOException {
    return List.of(
        new SystemMetrics(SystemMetricsLabel.DISK_FREE, Double.valueOf(file.getFreeSpace())),
        new SystemMetrics(SystemMetricsLabel.DISK_TOTAL, Double.valueOf(file.getTotalSpace())));
  }
}
