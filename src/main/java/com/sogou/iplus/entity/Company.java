/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.google.common.collect.Lists;

import commons.utils.JsonHelper;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月20日;
//-------------------------------------------------------
@ApiObject(description = "公司")
public class Company {

  public static final Company SOGOU = new Company();

  static {
    SOGOU.setName("搜狗");
    SOGOU.setKpis(Lists.newArrayList(1, 3, 13, 15, 21, 26, 37, 38, 39, 40, 41, 42, 43, 68));
  }

  @ApiObjectField(description = "公司id")
  private Integer id;

  @ApiObjectField(description = "公司名")
  private String name;

  @ApiObjectField(description = "公司kpi")
  private List<Integer> kpis;

  @ApiObjectField(description = "公司事业部")
  private List<BusinessUnit> businessUnits;

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

  public List<Integer> getKpis() {
    return kpis;
  }

  public void setKpis(List<Integer> kpis) {
    this.kpis = kpis;
  }

  public List<BusinessUnit> getBusinessUnits() {
    return businessUnits;
  }

  public void setBusinessUnits(List<BusinessUnit> businessUnits) {
    this.businessUnits = businessUnits;
  }

  @Override
  public String toString() {
    return JsonHelper.writeValueAsString(this);
  }

  public Company() {}

  public Company(Company company) {
    this.id = company.getId();
    this.name = company.getName();
    this.kpis = new ArrayList<>(company.getKpis());
    this.businessUnits = company.getBusinessUnits().stream().map(bu -> new BusinessUnit(bu))
        .collect(Collectors.toList());
  }

  public void remove(Set<Integer> kpiIds) {
    businessUnits.forEach(bu -> bu.remove(kpiIds));
    for (ListIterator<BusinessUnit> iterator = businessUnits.listIterator(); iterator.hasNext();)
      if (iterator.next().getProjects().isEmpty()) iterator.remove();
  }

}
