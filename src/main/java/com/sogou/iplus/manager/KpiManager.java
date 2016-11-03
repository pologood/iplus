/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.manager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
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
    List<Kpi> kpis = kpiMapper.select(null, null, date, true);
    kpis.forEach(already -> projectMap.get(already.getXmId()).getKpis()
        .removeIf(kpi -> Objects.equals(already.getKpiId(), kpi.getKpiId())));
    return new ApiResult<>(
        projectMap.values().stream().filter(project -> !project.getKpis().isEmpty()).collect(Collectors.toList()));
  }

  public ApiResult<?> selectWithDateAndXmId(Integer xmId, LocalDate date) {
    List<Kpi> kpis = kpiMapper.selectWithDateAndXmId(xmId, date, true);
    return new ApiResult<>(kpis.stream().collect(Collectors.toMap(kpi -> kpi.getKpiId(), kpi -> kpi.getKpi())));
  }

  public ApiResult<?> selectWithDateRangeAndKpiId(Integer xmId, List<Integer> kpiId, LocalDate beginDate,
      LocalDate endDate) {
    List<Kpi> kpis = kpiMapper.selectWithDateRangeAndKpiIds(xmId, kpiId, beginDate, endDate, true);
    Map<Integer, Map<LocalDate, BigDecimal>> result = new TreeMap<>();
    kpis.forEach(kpi -> result.computeIfAbsent(kpi.getKpiId(), k -> new TreeMap<>(Collections.reverseOrder()))
        .put(kpi.getCreateDate(), kpi.getKpi()));
    return new ApiResult<>(result);
  }

  @Transactional
  public ApiResult<?> addAll(LocalDate date) {
    Project.PROJECTS.forEach(project -> project.getKpis().forEach(kpi -> {
      try {
        Kpi toAdd = new Kpi(project.getXmId(), kpi.getKpiId(), new BigDecimal(Integer.MIN_VALUE),
            getKpiDate(kpi, date));
        toAdd.setCreateDate(date);
        kpiMapper.add(toAdd);
      } catch (DuplicateKeyException e) {}
    }));
    return ApiResult.ok();
  }

  public static LocalDate getKpiDate(Kpi kpi, LocalDate date) {
    return date.minusDays(kpi.getDay());
  }

}
