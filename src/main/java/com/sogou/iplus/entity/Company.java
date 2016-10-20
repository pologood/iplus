/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.entity;

import java.util.Set;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import commons.utils.JsonHelper;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月20日;
//-------------------------------------------------------
@ApiObject(description = "公司")
public class Company {

  public static final Company SOGOU = new Company();

  static {
    SOGOU.setName("搜狗");
  }

  @ApiObjectField(description = "公司id")
  private Integer id;

  @ApiObjectField(description = "公司名")
  private String name;

  @ApiObjectField(description = "公司kpi")
  private Set<Kpi> kpis;

  @ApiObjectField(description = "公司事业部")
  private Set<BusinessUnit> businessUnits;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Kpi> getKpis() {
    return kpis;
  }

  public void setKpis(Set<Kpi> kpis) {
    this.kpis = kpis;
  }

  public Set<BusinessUnit> getBusinessUnits() {
    return businessUnits;
  }

  public void setBusinessUnits(Set<BusinessUnit> businessUnits) {
    this.businessUnits = businessUnits;
  }

  @Override
  public String toString() {
    return JsonHelper.writeValueAsString(this);
  }

}
