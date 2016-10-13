/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.fasterxml.jackson.annotation.JsonIgnore;

import commons.utils.JsonHelper;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月11日;
//-------------------------------------------------------
@ApiObject(name = "kpi", description = "关键绩效指标")
public class Kpi {

  public Kpi() {
  }

  public Kpi(Integer xmId, Integer kpiId, String kpiName, BigDecimal kpi, LocalDate createDate) {
    this.xmId = xmId;
    this.kpiId = kpiId;
    this.kpiName = kpiName;
    this.kpi = kpi;
    this.createDate = createDate;
  }

  public Kpi(Kpi kpi) {
    this(kpi.getXmId(), kpi.getKpiId(), kpi.getKpiName(), kpi.getKpi(), kpi.getCreateDate());
  }

  public Kpi(int kpiId, String kpiName) {
    this(null, kpiId, kpiName, null, null);
  }

  public Kpi(int xmId, Integer kpiId, BigDecimal kpi, LocalDate createDate) {
    this(xmId, kpiId, null, kpi, createDate);
  }

  @JsonIgnore
  private Integer id;

  @ApiObjectField(description = "xmId", required = true)
  private Integer xmId;

  @ApiObjectField(description = "kpiId", required = true)
  private Integer kpiId;

  @ApiObjectField(description = "kpiName")
  private String kpiName;

  @ApiObjectField(description = "value", required = true)
  private BigDecimal kpi;

  @ApiObjectField(description = "日期")
  private LocalDate createDate;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getXmId() {
    return xmId;
  }

  public void setXmId(Integer xmId) {
    this.xmId = xmId;
  }

  public Integer getKpiId() {
    return kpiId;
  }

  public void setKpiId(Integer kpiId) {
    this.kpiId = kpiId;
  }

  public String getKpiName() {
    return kpiName;
  }

  public void setKpiName(String kpiName) {
    this.kpiName = kpiName;
  }

  public BigDecimal getKpi() {
    return kpi;
  }

  public void setKpi(BigDecimal value) {
    this.kpi = value;
  }

  public LocalDate getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDate createDate) {
    this.createDate = createDate;
  }

  @Override
  public String toString() {
    return JsonHelper.writeValueAsString(this);
  }

  @Override
  public int hashCode() {
    return kpiId.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (Objects.isNull(o) || !(o instanceof Kpi)) return false;
    Kpi another = (Kpi) o;
    return Objects.equals(this.kpiId, another.getKpiId()) && Objects.equals(this.kpi, another.getKpi())
        && Objects.equals(this.createDate, another.getCreateDate());
  }
}
