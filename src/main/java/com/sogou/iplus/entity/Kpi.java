/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.fasterxml.jackson.annotation.JsonIgnore;

import commons.utils.JsonHelper;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月11日;
//-------------------------------------------------------
@ApiObject(name = "kpi", description = "关键绩效指标")
public class Kpi {

  public Kpi() {}

  public Kpi(Integer xmId, Integer kpiId, String kpiName, BigDecimal kpi, LocalDate kpiDate, String shortName,
      Integer keySort, String shortName1) {
    this.xmId = xmId;
    this.kpiId = kpiId;
    this.kpiName = kpiName;
    this.kpi = kpi;
    this.kpiDate = kpiDate;
    this.shortName = shortName;
    this.keySort = keySort;
    this.shortName1 = shortName1;
  }

  public Kpi(Kpi kpi) {
    this(kpi.getXmId(), kpi.getKpiId(), kpi.getKpiName(), kpi.getKpi(), kpi.getKpiDate(), kpi.getShortName(),
        kpi.getKeySort(), kpi.getShortName1());
  }

  public Kpi(int kpiId, String kpiName, String shortName, Integer keySort, String shortName1) {
    this(null, kpiId, kpiName, null, null, shortName, keySort, shortName1);
  }

  public Kpi(int kpiId, String kpiName, String shortName, Integer keySort) {
    this(kpiId, kpiName, shortName, keySort, null);
  }

  public Kpi(int kpiId, String kpiName) {
    this(kpiId, kpiName, null);
  }

  public Kpi(int kpiId, String kpiName, String shortName1) {
    this(kpiId, kpiName, null, null, shortName1);
  }

  public Kpi(int xmId, Integer kpiId, BigDecimal kpi, LocalDate kpiDate) {
    this(xmId, kpiId, null, kpi, kpiDate, null, null, null);
  }

  @JsonIgnore
  private Integer id;

  @ApiObjectField(description = "xmId")
  private Integer xmId;

  @ApiObjectField(description = "kpiId")
  private Integer kpiId;

  @ApiObjectField(description = "kpiName")
  private String kpiName;

  @ApiObjectField(description = "shortName")
  private String shortName;

  @JsonIgnore
  private Integer keySort;

  @JsonIgnore
  private String shortName1;

  @ApiObjectField(description = "value")
  private BigDecimal kpi;

  @ApiObjectField(description = "日期")
  private LocalDate kpiDate;

  @JsonIgnore
  private LocalDate createDate;

  @JsonIgnore
  private LocalDateTime updateTime;

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

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public Integer getKeySort() {
    return keySort;
  }

  public void setKeySort(Integer keySort) {
    this.keySort = keySort;
  }

  public BigDecimal getKpi() {
    return kpi;
  }

  public void setKpi(BigDecimal value) {
    this.kpi = value;
  }

  public LocalDate getKpiDate() {
    return kpiDate;
  }

  public void setKpiDate(LocalDate kpiDate) {
    this.kpiDate = kpiDate;
  }

  public LocalDate getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDate createDate) {
    this.createDate = createDate;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }

  public String getShortName1() {
    return shortName1;
  }

  public void setShortName1(String shortName1) {
    this.shortName1 = shortName1;
  }

  @Override
  public String toString() {
    return JsonHelper.writeValueAsString(this);
  }

}
