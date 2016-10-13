/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.manager;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

  private static final Set<Kpi> EMPTY = new HashSet<>();

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
    Map<Integer, Set<Kpi>> kpiMap = Project.getKpiMap();
    List<Kpi> kpis = kpiMapper.selectKpisWithDate(date);
    kpis.forEach(already -> kpiMap.getOrDefault(already.getXmId(), EMPTY)
        .removeIf(kpi -> Objects.equals(already.getKpiId(), kpi.getKpiId())));
    return new ApiResult<>(kpiMap.entrySet().stream().filter(e -> !e.getValue().isEmpty())
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue())));
  }

}
