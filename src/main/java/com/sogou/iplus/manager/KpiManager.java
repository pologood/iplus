/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.manager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  public ApiResult<?> addOrUpdate(Set<Kpi> kpis) {
    if (CollectionUtils.isEmpty(kpis)) return ApiResult.badRequest("no valid kpi records");
    LOGGER.info("kpis to be recorded:{}", kpis);
    kpis.forEach(kpi -> addOrUpdate(kpi));
    return ApiResult.ok();
  }

  @Transactional
  public ApiResult<?> addOrUpdate(Kpi kpi) {
    Kpi local;
    if (Objects.isNull(local = kpiMapper.select(kpi))) kpiMapper.add(kpi);
    else if (!Objects.equals(kpi, local)) kpiMapper.update(kpi);
    return ApiResult.ok();
  }

  public ApiResult<?> selectProjectsDoNotSubmitKpiOnNamedDate(LocalDate date) {
    Map<Integer, Project> projectMap = Project.getProjectMap();
    List<Kpi> kpis = kpiMapper.selectKpisWithCreateDate(date);
    kpis.forEach(already -> projectMap.get(already.getXmId()).getKpis()
        .removeIf(kpi -> Objects.equals(already.getKpiId(), kpi.getKpiId())));
    return new ApiResult<>(
        projectMap.values().stream().filter(project -> !project.getKpis().isEmpty()).collect(Collectors.toList()));
  }

  public ApiResult<?> selectKpisWithDateAndXmId(Optional<Integer> xmId, LocalDate beginDate,
      LocalDate endDate) {
    Map<LocalDate, Map<Integer, Project>> map = new HashMap<>();
    List<Kpi> kpis = kpiMapper.selectKpisWithKpiDateRangeAndXmId(xmId.orElse(null), beginDate, endDate);
    kpis.forEach(
        kpi -> map.computeIfAbsent(kpi.getKpiDate(), k -> new HashMap<>()).computeIfAbsent(kpi.getXmId(), k -> {
          Project project = Project.getProjectByKpiId(kpi.getKpiId());
          project.getKpis().clear();
          return project;
        }).getKpis().add(kpi));
    return new ApiResult<>(
        map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().values())));
  }

  public ApiResult<?> selectKpisWithDateAndXmId(int xmId, LocalDate date) {
    List<Kpi> kpis = kpiMapper.selectKpisWithKpiDateAndXmId(xmId, date);
    return new ApiResult<>(kpis.stream().collect(Collectors.toMap(kpi -> kpi.getKpiId(), kpi -> kpi.getKpi())));
  }

}
