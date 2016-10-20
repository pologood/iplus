/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.entity;

import java.util.HashSet;
import java.util.Set;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.google.common.collect.Sets;

import commons.utils.JsonHelper;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月11日;
//-------------------------------------------------------
@ApiObject(description = "事业部")
public class BusinessUnit {

  public static final BusinessUnit SEARCH = new BusinessUnit(), DESKTOP = new BusinessUnit(),
      SUGARCAT = new BusinessUnit(), MARKETING = new BusinessUnit();

  static {
    SEARCH.setName("搜索事业部");
    DESKTOP.setName("桌面事业部");
    SUGARCAT.setName("糖猫事业部");
    MARKETING.setName("营销事业部");
    Company.SOGOU.setBusinessUnits(Sets.newHashSet(SEARCH, DESKTOP, SUGARCAT, MARKETING));
  }

  @ApiObjectField(description = "事业部id")
  private Integer id;

  @ApiObjectField(description = "事业部名称")
  private String name;

  @ApiObjectField(description = "事业部kpi")
  private Set<Kpi> kpis;

  @ApiObjectField(description = "事业部项目")
  private Set<Project> projects = new HashSet<>();

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

  public Set<Project> getProjects() {
    return projects;
  }

  public void setProjects(Set<Project> projects) {
    this.projects = projects;
  }

  @Override
  public String toString() {
    return JsonHelper.writeValueAsString(this);
  }

}
