package com.sahlone.app.smc.models;

public enum SystemMetricsLabel {
  CPU_TOTAL("cpu_total"),
  CPU_USED("cpu_used"),
  DISK_TOTAL("disk_space_total"),
  DISK_FREE("disk_space_free"),
  SYS_MEMORY_TOTAL("sys_mem_total"),
  SYS_MEMORY_FREE("sys_mem_free");

  public String name;

  SystemMetricsLabel(String name) {
    this.name = name;
  }
}
