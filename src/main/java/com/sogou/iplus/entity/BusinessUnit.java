/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

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
    SEARCH.setKpis(Arrays.asList(37, 38, 39, 40, 41, 42, 43, 49, 50, 51, 52, 53, 54, 59, 63, 207, 208));
    DESKTOP.setName("桌面事业部");
    DESKTOP.setKpis(Arrays.asList(1, 3, 13, 15, 21, 26, 27, 32));
    SUGARCAT.setName("糖猫事业部");
    MARKETING.setName("营销事业部");
    Company.SOGOU.setBusinessUnits(Arrays.asList(DESKTOP, SEARCH, MARKETING, SUGARCAT));
  }

  @ApiObjectField(description = "事业部id")
  private Integer id;

  @ApiObjectField(description = "事业部名称")
  private String name;

  @ApiObjectField(description = "事业部kpi")
  private List<Integer> kpis = new ArrayList<>();

  @ApiObjectField(description = "事业部项目")
  private List<Project> projects = new ArrayList<>();

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

  public List<Project> getProjects() {
    return projects;
  }

  public void setProjects(List<Project> projects) {
    this.projects = projects;
  }

  @Override
  public String toString() {
    return JsonHelper.writeValueAsString(this);
  }

  public BusinessUnit() {}

  public BusinessUnit(BusinessUnit bu) {
    this.id = bu.getId();
    this.name = bu.getName();
    this.kpis = new ArrayList<>(bu.getKpis());
    this.projects = bu.getProjects().stream().map(p -> new Project(p)).collect(Collectors.toList());
  }

  public void remove(Set<Integer> kpiIds) {
    projects.forEach(p -> p.remove(kpiIds, false));
    List<Project> list = new ArrayList<>();
    projects.stream().filter(p -> p.getKpis().size() > 0).forEach(p -> list.add(p));
    projects = list;
  }
}
