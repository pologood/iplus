/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

import commons.utils.JsonHelper;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月11日;
//-------------------------------------------------------
@ApiObject(name = "project", description = "项目")
public class Project {
  public @JsonIgnore transient static final List<Project> PROJECTS = new ArrayList<>();
  public @JsonIgnore transient static final Map<Integer, Project> PROJECT_MAP;
  public @JsonIgnore transient static final Map<Integer, Kpi> KPI_MAP = new HashMap<>();

  public static final Project PC_INPUT, MOBILE_INPUT, QQ_INPUT, PC_BROWSER, MOBILE_BROWSER, NAVIGATION, VOICE, NEWS,
      PEDIA, CHINESE_MEDICINE, MAP, APP_MARKET, PC_SEARCH, WIRELESS_SEARCH, SEARCH_APP, VEDIO_SEARCH, PICTURE_SEARCH,
      SHOPPING_SEARCH, NOVEL_SEARCH, APP_SEARCH, SUGAR_CAT;

  static {
    PROJECTS.add(new Project(0, "i4zqbwchh23igzwe", "test", new ArrayList<>(), null));
    PROJECTS.add(PC_INPUT = new Project(70, "j0a37izra1v4n4k0",
        "输入法-PC输入法", Lists.newArrayList(new Kpi(1, "PC输入法日活跃用户数(万)", "活跃用户", 1),
            new Kpi(2, "PC输入法灵犀日搜索量(万)", "灵犀搜索量", 2), new Kpi(10, "PC输入法第7日留存率"), new Kpi(18, "PC输入法第30日留存率")),
        BusinessUnit.DESKTOP));
    PROJECTS.add(MOBILE_INPUT = new Project(78, "mxvqf109b7kbyfad", "输入法-手机输入法",
        Lists.newArrayList(new Kpi(3, "手机输入法日活跃用户数(万)", "活跃用户", 1), new Kpi(4, "手机输入法iOS版日活跃用户数(万)"),
            new Kpi(5, "手机输入法Android版日活跃用户数(万)"), new Kpi(6, "手机输入法灵犀日搜索量(万)"), new Kpi(7, "手机输入法日激活量(万)", "新增用户", 2),
            new Kpi(101, "手机输入法Android版次日留存率"), new Kpi(102, "手机输入法Android版第7日留存率"),
            new Kpi(103, "手机输入法Android版第30日留存率"), new Kpi(104, "手机输入法iOS版次日留存率"), new Kpi(105, "手机输入法iOS版第7日留存率"),
            new Kpi(106, "手机输入法iOS版第30日留存率")),
        BusinessUnit.DESKTOP));
    PROJECTS.add(QQ_INPUT = new Project(157, "u9h63f6k3kxy1qfc", "输入法-QQ输入法",
        Lists.newArrayList(new Kpi(11, "QQ输入法Windows版日活跃用户数(万)", "Windows日活", 1),
            new Kpi(12, "QQ输入法Android版日活跃用户数(万)", "Android日活", 2), new Kpi(19, "QQ输入法Windows版第7日留存率"),
            new Kpi(34, "QQ输入法Windows版第30日留存率"), new Kpi(35, "QQ输入法Android版次日留存率"), new Kpi(55, "QQ输入法Android版第7日留存率")),
        BusinessUnit.DESKTOP));
    PROJECTS.add(PC_BROWSER = new Project(68, "2ej50d9vfy9aa486", "浏览器-PC浏览器", Lists
        .newArrayList(new Kpi(13, "PC浏览器日主动活跃用户数(万)", "活跃用户", 1), new Kpi(14, "搜狗PC浏览器网页搜索量-不含灵犀分流量(万)", "PC搜索量", 2)),
        BusinessUnit.DESKTOP));
    PROJECTS.add(MOBILE_BROWSER = new Project(76, "423ftqdz3cyjpa14", "浏览器-手机浏览器",
        Lists.newArrayList(new Kpi(15, "手机浏览器日活跃用户数(万)", "活跃用户", 1), new Kpi(16, "手机浏览器给搜索带去的日搜索量(万)"),
            new Kpi(17, "手机浏览器日激活量(万)", "新增用户", 2), new Kpi(107, "手机浏览器Android版次日留存率"),
            new Kpi(108, "手机浏览器Android版第7日留存率"), new Kpi(109, "手机浏览器Android版第30日留存率"), new Kpi(110, "手机浏览器iOS版次日留存率"),
            new Kpi(111, "手机浏览器iOS版第7日留存率"), new Kpi(112, "手机浏览器iOS版第30日留存率")),
        BusinessUnit.DESKTOP));
    PROJECTS.add(NAVIGATION = new Project(260, "zmit8fzhdlyqj5hd", "导航",
        Lists.newArrayList(new Kpi(21, "导航合计用户数(万)", "活跃用户", 1), new Kpi(22, "搜狗导航自有活跃用户数(万)", "自有日活", 2),
            new Kpi(23, "搜狗导航外购活跃用户数(万)"), new Kpi(24, "QQ独立导航活跃用户数(万)"), new Kpi(25, "QQ浏览器中QQ导航活跃用户数(万)")),
        BusinessUnit.DESKTOP));
    PROJECTS.add(VOICE = new Project(82, "95lykq31ci0fw5p7", "语音",
        Lists.newArrayList(new Kpi(26, "语音日请求量(万)", "日请求量", 1)), BusinessUnit.DESKTOP));
    PROJECTS
        .add(NEWS = new Project(322, "hw9cfh7zaor7kuwd", "个性化阅读",
            Lists.newArrayList(new Kpi(27, "个性化阅读日活跃用户数(万)", "活跃用户", 1), new Kpi(28, "个性化阅读日激活量(万)", "新增用户", 2),
                new Kpi(29, "个性化阅读次日留存率"), new Kpi(30, "个性化阅读第7日留存率"), new Kpi(31, "个性化阅读第30日留存率")),
            BusinessUnit.DESKTOP));
    PROJECTS.add(MAP = new Project(148, "h0wb4btxx2xm56xp", "手机地图",
        Lists.newArrayList(new Kpi(32, "手机地图日活跃用户数(万)", "活跃用户", 1), new Kpi(33, "手机地图日激活量(万)", "新增用户", 2),
            new Kpi(113, "手机地图Android版次日留存率"), new Kpi(114, "手机地图Android版7日留存率"), new Kpi(115, "手机地图Android版30日留存率"),
            new Kpi(116, "手机地图iOS版次日留存率"), new Kpi(117, "手机地图iOS版7日留存率"), new Kpi(118, "手机地图iOS版30日留存率")),
        BusinessUnit.DESKTOP));
    PROJECTS.add(APP_MARKET = new Project(11, "ljy4vpeq567fejbj", "手机助手", Lists
        .newArrayList(new Kpi(8, "手机助手日活跃用户数(万)", "活跃用户", 1), new Kpi(9, "手机助手日下载量(万)", "下载量", 2)),
        BusinessUnit.DESKTOP));
    PROJECTS.add(PC_SEARCH = new Project(137, "sp6jmm92s2p1u8qn", "PC网页搜索",
        Lists.newArrayList(new Kpi(37, "PC搜狗自有渠道日搜索量(万)", "PC搜索量", 1), new Kpi(38, "PC腾讯渠道日搜索量(万)", "PC腾讯", 2),
            new Kpi(39, "PC外购网页日搜索量(万)")),
        BusinessUnit.SEARCH));
    PROJECTS.add(WIRELESS_SEARCH = new Project(20, "z1fv4fciko2gwurq", "无线网页搜索",
        Lists.newArrayList(new Kpi(40, "无线搜狗自有渠道日搜索量(万)", "搜狗自有", 1), new Kpi(41, "无线腾讯渠道日搜索量(万)", "腾讯渠道", 2),
            new Kpi(42, "外购网页无线日搜索量(万)")),
        BusinessUnit.SEARCH));
    PROJECTS.add(SEARCH_APP = new Project(232, "sohampb0dogwgxuz", "搜索APP",
        Lists.newArrayList(new Kpi(43, "搜索APP日活跃用户数(万)", "活跃用户", 1), new Kpi(44, "搜索APP日搜索量(万)"),
            new Kpi(45, "搜索APP日激活量(万)", "新增用户", 2), new Kpi(46, "搜索APP次日留存率"), new Kpi(47, "搜索APP第7日留存率"),
            new Kpi(48, "搜索APP第30日留存率")),
        BusinessUnit.SEARCH));
    PROJECTS.add(VEDIO_SEARCH = new Project(141, "t4nnt1t5a055mjch", "视频搜索",
        Lists.newArrayList(new Kpi(49, "视频搜索(PC+无线)用户量(万)", "活跃用户", 1)), BusinessUnit.SEARCH));
    PROJECTS.add(PICTURE_SEARCH = new Project(138, "wzze931ap6087cf7", "图片搜索",
        Lists.newArrayList(new Kpi(50, "图片搜索(PC+无线)用户量(万)", "活跃用户", 1)), BusinessUnit.SEARCH));
    PROJECTS.add(APP_SEARCH = new Project(27, "iepyipalep95q36u", "应用搜索",
        Lists.newArrayList(new Kpi(51, "应用搜索日分发量(万)", "应用分发", 1), new Kpi(52, "软件搜索日分发量(万)", "软件分发", 2)),
        BusinessUnit.SEARCH));
    PROJECTS.add(SHOPPING_SEARCH = new Project(142, "hxuuaa7ylg2tizpp", "购物搜索",
        Lists.newArrayList(new Kpi(53, "PC+无线购物搜索用户量(万)", "活跃用户", 1)), BusinessUnit.SEARCH));
    PROJECTS.add(NOVEL_SEARCH = new Project(31, "1k5rg0vny4y1htl2", "小说搜索",
        Lists.newArrayList(new Kpi(54, "WAP书城用户量(万)"), new Kpi(207, "阅读APPAndroid版日活跃用户数(万)", "Android日活", 1),
            new Kpi(208, "阅读APPiOS版日活跃用户数(万)", "iOS日活", 2), new Kpi(201, "阅读APPAndroid版次日留存率"),
            new Kpi(202, "阅读APPAndroid版第7日留存率"), new Kpi(203, "阅读APPAndroid版第30日留存率"), new Kpi(204, "阅读APPiOS版次日留存率"),
            new Kpi(205, "阅读APPiOS版第7日留存率"), new Kpi(206, "阅读APPiOS版第30日留存率")),
        BusinessUnit.SEARCH));
    PROJECTS.add(PEDIA = new Project(116,
        "4t8hnsfb3igf9p91", "百科、问问", Lists.newArrayList(new Kpi(59, "百科:百科UV(PC+无线)", "百科日活", 1),
            new Kpi(60, "百科:日编辑版本数(万)"), new Kpi(61, "问问:有效写入量(万)"), new Kpi(62, "问问:用户规模(PV)", "问问日活", 2)),
        BusinessUnit.SEARCH));
    PROJECTS.add(CHINESE_MEDICINE = new Project(306, "zvn6s7zwq56ri18y", "中医项目",
        Lists.newArrayList(new Kpi(63, "中医项目App日活(个)", "活跃用户", 1), new Kpi(64, "中医项目App次日留存率"),
            new Kpi(65, "中医项目App第7日留存率"), new Kpi(66, "中医项目App第30日留存率"), new Kpi(67, "中医项目App总激活数(个)", "累计用户", 2)),
        BusinessUnit.SEARCH));
    PROJECTS.add(new Project(36, "1ou8k1pdoe4ac3lz", "消耗、RPM",
        Lists.newArrayList(new Kpi(68, "全部竞价日消耗(万元)", "总日消耗", 1), new Kpi(69, "PC搜索日消耗(万元)"),
            new Kpi(70, "无线搜索日消耗(万元)"), new Kpi(71, "网盟日消耗(万元)"), new Kpi(72, "银河皓月日消耗(万元)"),
            new Kpi(73, "PC搜狗浏览器起始页RPM(元/千次)"), new Kpi(74, "无线QQ浏览器RPM(元/千次)"), new Kpi(75, "PC搜索RPM(优质)(元/千次)"),
            new Kpi(76, "无线搜索RPM(元/千次)", "无线RPM", 2)),
        BusinessUnit.MARKETING));
    PROJECTS.add(SUGAR_CAT = new Project(42, "qfysv8ha64crrdsc", "糖猫",
        Lists.newArrayList(new Kpi(77, "糖猫日新激活(个)", "新增用户", 1), new Kpi(88, "糖猫T1日新激活(个)"), new Kpi(89, "糖猫T2日新激活(个)"),
            new Kpi(90, "糖猫E1日新激活(个)"), new Kpi(91, "糖猫M1日新激活(个)"), new Kpi(92, "糖猫BASIC日新激活(个)"),
            new Kpi(78, "糖猫日活跃用户(个)", "活跃用户", 2), new Kpi(83, "糖猫T1日活跃用户(个)"), new Kpi(84, "糖猫T2日活跃用户(个)"),
            new Kpi(85, "糖猫E1日活跃用户(个)"), new Kpi(86, "糖猫M1日活跃用户(个)"), new Kpi(87, "糖猫BASIC日活跃用户(个)"),
            new Kpi(79, "糖猫日活跃APP用户(个)"), new Kpi(80, "糖猫APP次日留存率"), new Kpi(81, "糖猫APP第7日留存率"),
            new Kpi(82, "糖猫APP第30日留存率")),
        BusinessUnit.SUGARCAT));

    PROJECT_MAP = getProjectMap();

    PROJECTS.stream().filter(p -> Objects.nonNull(p.getBusinessUnit()))
        .forEach(project -> project.getBusinessUnit().getProjects().add(project));

    PROJECTS.forEach(project -> project.getKpis().forEach(kpi -> KPI_MAP.put(kpi.getKpiId(), kpi)));
  }

  public static Map<Integer, Project> getProjectMap() {
    return PROJECTS.stream().collect(Collectors.toMap(p -> p.getXmId(), p -> new Project(p)));
  }

  public static Kpi getKpi(Integer kpiId) {
    return KPI_MAP.get(kpiId);
  }

  @Override
  public String toString() {
    return JsonHelper.writeValueAsString(this);
  }

  public Project(Integer xmId, String xmKey, String projectName, List<Kpi> kpis, BusinessUnit businessUnit) {
    this.xmId = xmId;
    this.xmKey = xmKey;
    this.projectName = projectName;
    this.kpis = kpis;
    this.businessUnit = businessUnit;
    this.keyKpis = this.kpis.stream().filter(kpi -> Objects.nonNull(kpi.getShortName()))
        .sorted(Comparator.comparingInt(k -> k.getKeySort())).collect(Collectors.toList());
  }

  public Project(Project project) {
    this(project.getXmId(), project.getXmKey(), project.getProjectName(),
        project.getKpis().stream().map(kpi -> new Kpi(kpi)).collect(Collectors.toList()), project.getBusinessUnit());
  }

  @ApiObjectField(description = "项目id", required = true)
  private Integer xmId;

  @JsonIgnore
  private String xmKey;

  @ApiObjectField(description = "项目名称")
  private String projectName;

  @ApiObjectField(description = "kpi", required = true)
  private List<Kpi> kpis;

  @JsonIgnore
  private BusinessUnit businessUnit;

  private List<Kpi> keyKpis;

  public Integer getXmId() {
    return xmId;
  }

  public void setXmId(Integer xmId) {
    this.xmId = xmId;
  }

  public String getXmKey() {
    return xmKey;
  }

  public void setXmKey(String xmKey) {
    this.xmKey = xmKey;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public List<Kpi> getKpis() {
    return kpis;
  }

  public void setKpis(List<Kpi> kpis) {
    this.kpis = kpis;
  }

  public BusinessUnit getBusinessUnit() {
    return this.businessUnit;
  }

  public void setBusinessUnit(BusinessUnit businessUnit) {
    this.businessUnit = businessUnit;
  }

  public List<Kpi> getKeyKpis() {
    return keyKpis;
  }

  public void setKeyKpis(List<Kpi> keyKpis) {
    this.keyKpis = keyKpis;
  }
}
