/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;

import commons.utils.JsonHelper;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月11日;
//-------------------------------------------------------
@ApiObject(name = "project", description = "项目")
public class Project {

  public Project(Integer projectId, String projectKey, String projectName, Set<Kpi> kpis, BusinessUnit businessUnit) {
    this.projectId = projectId;
    this.projectKey = projectKey;
    this.projectName = projectName;
    this.kpis = kpis;
    this.businessUnit = businessUnit;
  }

  public Project(Project project) {
    this(project.getProjectId(), project.getProjectKey(), project.getProjectName(),
        project.getKpis().stream().map(kpi -> new Kpi(kpi)).collect(Collectors.toSet()), project.getBusinessUnit());
  }

  @ApiObjectField(description = "项目id", required = true)
  private Integer projectId;

  @JsonIgnore
  private String projectKey;

  @ApiObjectField(description = "项目名称")
  private String projectName;

  @ApiObjectField(description = "kpi", required = true)
  private Set<Kpi> kpis;

  @JsonIgnore
  private BusinessUnit businessUnit;

  public Integer getProjectId() {
    return projectId;
  }

  public void setProjectId(Integer projectId) {
    this.projectId = projectId;
  }

  public String getProjectKey() {
    return projectKey;
  }

  public void setProjectKey(String projectKey) {
    this.projectKey = projectKey;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public Set<Kpi> getKpis() {
    return kpis;
  }

  public void setKpis(Set<Kpi> kpis) {
    this.kpis = kpis;
  }

  public BusinessUnit getBusinessUnit() {
    return this.businessUnit;
  }

  public void setBusinessUnit(BusinessUnit businessUnit) {
    this.businessUnit = businessUnit;
  }

  @JsonIgnore
  public transient static final Set<Project> PROJECTS = new HashSet<>();

  @JsonIgnore
  public transient static final Map<Integer, Project> PROJECT_MAP;

  @JsonIgnore
  public transient static final Map<Integer, Project> KPI_MAP = new HashMap<>();

  static {
    PROJECTS.add(new Project(70, "j0a37izra1v4n4k0", "输入法-PC输入法",
        Sets.newHashSet(new Kpi(1, "PC输入法日活跃用户数(万)"), new Kpi(2, "PC输入法灵犀日搜索量(万)")), BusinessUnit.DESKTOP));
    PROJECTS.add(new Project(78, "mxvqf109b7kbyfad", "输入法-手机输入法",
        Sets.newHashSet(new Kpi(3, "手机输入法日活跃用户数(万)"), new Kpi(4, "手机输入法iOS版日活跃用户数(万)"),
            new Kpi(5, "手机输入法Android版日活跃用户数(万)"), new Kpi(6, "手机输入法灵犀日搜索量(万)"), new Kpi(7, "手机输入法日激活量(万)"),
            new Kpi(101, "手机输入法Android版次日留存率"), new Kpi(102, "手机输入法Android版第7日留存率"),
            new Kpi(103, "手机输入法Android版第30日留存率"), new Kpi(104, "手机输入法iOS版次日留存率"), new Kpi(105, "手机输入法iOS版第7日留存率"),
            new Kpi(106, "手机输入法iOS版第30日留存率")),
        BusinessUnit.DESKTOP));
    PROJECTS.add(new Project(157, "u9h63f6k3kxy1qfc", "输入法-QQ输入法",
        Sets.newHashSet(new Kpi(11, "QQ输入法Windows版日活跃用户数(万)"), new Kpi(12, "QQ输入法Android版日活跃用户数(万)")),
        BusinessUnit.DESKTOP));
    PROJECTS.add(new Project(68, "2ej50d9vfy9aa486", "浏览器-PC浏览器",
        Sets.newHashSet(new Kpi(13, "PC浏览器日主动活跃用户数(万)"), new Kpi(14, "PC浏览器给搜索带来的自有网页PC搜索日搜索量(万)")),
        BusinessUnit.DESKTOP));
    PROJECTS.add(new Project(76, "423ftqdz3cyjpa14", "浏览器-手机浏览器",
        Sets.newHashSet(new Kpi(15, "手机浏览器日活跃用户数(万)"), new Kpi(16, "手机浏览器给搜索带去的日搜索量(万)"), new Kpi(17, "手机浏览器日激活量(万)"),
            new Kpi(107, "手机浏览器Android版次日留存率"), new Kpi(108, "手机浏览器Android版第7日留存率"),
            new Kpi(109, "手机浏览器Android版第30日留存率"), new Kpi(110, "手机浏览器iOS版次日留存率"), new Kpi(111, "手机浏览器iOS版第7日留存率"),
            new Kpi(112, "手机浏览器iOS版第30日留存率")),
        BusinessUnit.DESKTOP));
    PROJECTS.add(new Project(260,
        "zmit8fzhdlyqj5hd", "导航", Sets.newHashSet(new Kpi(21, "导航合计用户数(万)"), new Kpi(22, "搜狗导航自有活跃用户数(万)"),
            new Kpi(23, "搜狗导航外购活跃用户数(万)"), new Kpi(24, "QQ独立导航活跃用户数(万)"), new Kpi(25, "QQ浏览器中QQ导航活跃用户数(万)")),
        BusinessUnit.DESKTOP));
    PROJECTS.add(
        new Project(82, "95lykq31ci0fw5p7", "语音", Sets.newHashSet(new Kpi(26, "语音日请求量(万)")), BusinessUnit.DESKTOP));
    PROJECTS
        .add(new Project(322, "hw9cfh7zaor7kuwd",
            "个性化", Sets.newHashSet(new Kpi(27, "个性化阅读日活跃用户数(万)"), new Kpi(28, "个性化阅读日激活量(万)"),
                new Kpi(29, "个性化阅读次日留存率"), new Kpi(30, "个性化阅读第7日留存率"), new Kpi(31, "个性化阅读第30日留存率")),
        BusinessUnit.DESKTOP));
    PROJECTS.add(new Project(148, "h0wb4btxx2xm56xp", "手机地图",
        Sets.newHashSet(new Kpi(32, "手机地图日活跃用户数(万)"), new Kpi(33, "手机地图日激活量(万)"), new Kpi(113, "手机地图Android版次日留存率"),
            new Kpi(114, "手机地图Android版7日留存率"), new Kpi(115, "手机地图Android版30日留存率"), new Kpi(116, "手机地图iOS版次日留存率"),
            new Kpi(117, "手机地图iOS版7日留存率"), new Kpi(118, "手机地图iOS版30日留存率")),
        BusinessUnit.DESKTOP));
    PROJECTS.add(new Project(137, "sp6jmm92s2p1u8qn", "PC网页搜索",
        Sets.newHashSet(new Kpi(37, "PC搜狗自有渠道日搜索量(万)"), new Kpi(38, "PC腾讯渠道日搜索量(万)"), new Kpi(39, "PC外购网页日搜索量(万)")),
        BusinessUnit.SEARCH));
    PROJECTS.add(new Project(20, "z1fv4fciko2gwurq", "无线网页搜索",
        Sets.newHashSet(new Kpi(40, "无线搜狗自有渠道日搜索量(万)"), new Kpi(41, "无线腾讯渠道日搜索量(万)"), new Kpi(42, "外购网页无线日搜索量(万)")),
        BusinessUnit.SEARCH));
    PROJECTS
        .add(new Project(232, "sohampb0dogwgxuz", "搜索APP",
            Sets.newHashSet(new Kpi(43, "搜索APP日活跃用户数(万)"), new Kpi(44, "搜索APP日搜索量(万)"), new Kpi(45, "搜索APP日激活量(万)"),
                new Kpi(46, "搜索APP次日留存率"), new Kpi(47, "搜索APP7日留存率"), new Kpi(48, "搜索APP30日留存率")),
        BusinessUnit.SEARCH));
    PROJECTS.add(new Project(141, "t4nnt1t5a055mjch", "视频搜索", Sets.newHashSet(new Kpi(49, "视频搜索(PC+无线)用户量(万)")),
        BusinessUnit.SEARCH));
    PROJECTS.add(new Project(138, "wzze931ap6087cf7", "图片搜索", Sets.newHashSet(new Kpi(50, "图片搜索(PC+无线)用户量(万)")),
        BusinessUnit.SEARCH));
    PROJECTS.add(new Project(27, "iepyipalep95q36u", "应用搜索",
        Sets.newHashSet(new Kpi(51, "应用搜索日分发量(万)"), new Kpi(52, "软件搜索日分发量(万)")), BusinessUnit.SEARCH));
    PROJECTS.add(new Project(142, "hxuuaa7ylg2tizpp", "购物搜索", Sets.newHashSet(new Kpi(53, "PC+无线购物搜索用户量(万)")),
        BusinessUnit.SEARCH));
    PROJECTS.add(new Project(31, "1k5rg0vny4y1htl2", "小说搜索",
        Sets.newHashSet(new Kpi(54, "小说搜索搜索量(万)"), new Kpi(207, "阅读APPAndroid版日活跃用户数(万)"),
            new Kpi(208, "阅读APPiOS版日活跃用户数(万)"), new Kpi(201, "阅读APPAndroid版次日留存率"), new Kpi(202, "阅读APPAndroid版7日留存率"),
            new Kpi(203, "阅读APPAndroid版30日留存率"), new Kpi(204, "阅读APPiOS版次日留存率"), new Kpi(205, "阅读APPiOS版7日留存率"),
            new Kpi(206, "阅读APPiOS版30日留存率")),
        BusinessUnit.SEARCH));
    PROJECTS
        .add(
            new Project(
                116, "4t8hnsfb3igf9p91", "百科、问问", Sets.newHashSet(new Kpi(59, "百科:百科UV(PC+无线)"),
                    new Kpi(60, "百科:月编辑版本数"), new Kpi(61, "问问:有效写入量(日均)"), new Kpi(62, "问问:用户规模(PV)")),
            BusinessUnit.SEARCH));
    PROJECTS.add(new Project(306,
        "zvn6s7zwq56ri18y", "中医项目", Sets.newHashSet(new Kpi(63, "中医项目App日活"), new Kpi(64, "中医项目App次日留存率"),
            new Kpi(65, "中医项目App7日留存率"), new Kpi(66, "中医项目App30日留存率"), new Kpi(67, "中医项目App总激活数")),
        BusinessUnit.SEARCH));
    PROJECTS.add(new Project(36, "1ou8k1pdoe4ac3lz", "消耗、RPM",
        Sets.newHashSet(new Kpi(68, "全部竞价日消耗(万元)"), new Kpi(69, "PC搜索日消耗(万元)"), new Kpi(70, "无线搜索日消耗(万元)"),
            new Kpi(71, "网盟日消耗(万元)"), new Kpi(72, "银河皓月日消耗(万元)"), new Kpi(73, "PC搜狗浏览器起始页RPM(元/千次)"),
            new Kpi(74, "无线QQ浏览器RPM(元/千次)"), new Kpi(75, "PC搜索RPM(优质)(元/千次)"), new Kpi(76, "无线搜索RPM(元/千次)")),
        BusinessUnit.MARKETING));
    PROJECTS
        .add(new Project(42, "qfysv8ha64crrdsc", "糖猫",
            Sets.newHashSet(new Kpi(77, "糖猫日新激活(个)"), new Kpi(78, "糖猫日活跃用户(个)"), new Kpi(79, "糖猫日活跃APP用户(个)"),
                new Kpi(80, "糖猫APP次日留存率"), new Kpi(81, "糖猫APP7日留存率"), new Kpi(82, "糖猫APP30日留存率")),
        BusinessUnit.SUGARCAT));

    PROJECT_MAP = getProjectMap();

    PROJECTS.forEach(project -> project.kpis.forEach(kpi -> KPI_MAP.put(kpi.getKpiId(), project)));

    PROJECTS.forEach(project -> project.getBusinessUnit().getProjects().add(project));
  }

  public static Project getProjectByKpiId(int kpiId) {
    Project project = KPI_MAP.get(kpiId);
    return Objects.isNull(project) ? null : new Project(project);
  }

  public static Map<Integer, Project> getProjectMap() {
    return PROJECTS.stream().collect(Collectors.toMap(p -> p.getProjectId(), p -> new Project(p)));
  }

  @Override
  public String toString() {
    return JsonHelper.writeValueAsString(this);
  }
}
