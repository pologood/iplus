/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sogou.iplus.entity.Kpi;
import com.sogou.iplus.entity.Project;
import com.sogou.iplus.mapper.KpiMapper;
import com.sogou.iplus.model.ApiResult;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月11日;
//-------------------------------------------------------
@Service
public class KpiManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(KpiManager.class);

  @Autowired
  private KpiMapper kpiMapper;

  @Transactional
  public ApiResult<?> update(Set<Kpi> kpis) {
    if (CollectionUtils.isEmpty(kpis)) return ApiResult.badRequest("no valid kpi records");
    LOGGER.info("kpis to be recorded:{}", kpis);
    kpis.forEach(kpi -> kpiMapper.update(kpi));
    return ApiResult.ok();
  }

  public ApiResult<?> selectProjectsDoNotSubmitKpiOnNamedDate(LocalDate date) {
    Map<Integer, Project> projectMap = Project.getProjectMap();
    List<Kpi> kpis = select(null, null, date);
    kpis.forEach(already -> projectMap.get(already.getXmId()).getKpis()
        .removeIf(kpi -> Objects.equals(already.getKpiId(), kpi.getKpiId())));
    return new ApiResult<>(
        projectMap.values().stream().filter(project -> !project.getKpis().isEmpty()).collect(Collectors.toList()));
  }

  public ApiResult<?> selectWithDateAndXmId(Integer xmId, LocalDate date) {
    List<Kpi> kpis = select(xmId, null, date);
    return new ApiResult<>(kpis.stream().collect(Collectors.toMap(k -> k.getKpiId(), k -> k.getKpi())));
  }

  public ApiResult<?> selectWithDateRangeAndKpiId(Integer xmId, List<Integer> kpiId, LocalDate beginDate,
      LocalDate endDate) {
    List<Kpi> kpis = kpiMapper.select(xmId, kpiId, beginDate, endDate, false);
    Map<Integer, Map<LocalDate, Kpi>> result = new TreeMap<>();
    kpis.forEach(kpi -> result.computeIfAbsent(kpi.getKpiId(), k -> new TreeMap<>(Comparator.reverseOrder()))
        .put(kpi.getCreateDate(), kpi));
    return new ApiResult<>(result);
  }

  public ApiResult<?> getAverage(List<Integer> list, Integer xmId, List<String> kpiIds, LocalDate beginDate,
      LocalDate endDate) {
    List<Kpi> kpis = select(xmId, list, beginDate, endDate);
    Map<String, Map<LocalDate, BigDecimal>> map = new HashMap<>();
    kpis.forEach(kpi -> map.computeIfAbsent(getUnionKpi(kpi.getKpiId().toString(), kpiIds), k -> new HashMap<>())
        .put(kpi.getCreateDate(), kpi.getKpi()));
    Map<String, BigDecimal> result = new TreeMap<>();
    map.entrySet().forEach(e -> result.put(e.getKey(), getAverage(e.getValue())));
    return new ApiResult<>(result);
  }

  private String getUnionKpi(String kpiId, List<String> kpiIds) {
    return kpiIds.stream().filter(id -> id.contains(kpiId)).findAny().get();
  }

  private BigDecimal getAverage(Map<LocalDate, BigDecimal> map) {
    BigDecimal sum = BigDecimal.ZERO;
    if (map.isEmpty()) return sum;
    for (BigDecimal v : map.values())
      sum = sum.add(v);
    return sum.divide(new BigDecimal(map.size()), 4, RoundingMode.HALF_UP);
  }

  @Transactional
  public ApiResult<?> addAll(LocalDate date) {
    Project.PROJECTS.forEach(project -> project.getKpis().forEach(kpi -> {
      try {
        Kpi toAdd = new Kpi(project.getXmId(), kpi.getKpiId(), new BigDecimal(-1), getKpiDate(kpi, date));
        toAdd.setCreateDate(date);
        kpiMapper.add(toAdd);
      } catch (DuplicateKeyException e) {}
    }));
    return ApiResult.ok();
  }

  public static LocalDate getKpiDate(Kpi kpi, LocalDate date) {
    int day = 1;
    if (kpi.getKpiName().indexOf("次日") != -1) day = 2;
    else if (kpi.getKpiName().indexOf("7日") != -1) day = 8;
    else if (kpi.getKpiName().indexOf("30日") != -1) day = 31;
    return date.minusDays(day);
  }

  List<Kpi> select(Integer xmId, List<Integer> kpiId, LocalDate date) {
    return select(xmId, kpiId, date, date);
  }

  List<Kpi> select(Integer xmId, List<Integer> kpiId, LocalDate begin, LocalDate end) {
    return kpiMapper.select(xmId, kpiId, begin, end, true);
  }

}
