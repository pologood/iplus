/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sogou.iplus.entity.Kpi;
import com.sogou.iplus.entity.Project;
import com.sogou.iplus.manager.KpiManager;
import com.sogou.iplus.model.ApiResult;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月11日;
//-------------------------------------------------------
@Api(name = "kpi API", description = "Read/Write/Update/Delete the kpi")
@RestController
@RequestMapping("/api")
public class KpiController {

  @Autowired
  private KpiManager kpiManager;

  @ApiMethod(description = "add kpi record")
  @RequestMapping(value = "/kpi", method = RequestMethod.PUT)
  public ApiResult<?> add(HttpServletRequest request,
      @ApiQueryParam(name = "xmId", description = "项目id") @RequestParam int xmId,
      @ApiQueryParam(name = "xmKey", description = "项目秘钥") @RequestParam String xmKey,
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd", required = false) @RequestParam @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> date) {
    Map<String, Project> map = Project.PROJECT_MAP.get(xmId);
    if (MapUtils.isEmpty(map)) return ApiResult.badRequest("invalid xmId");
    Project project = map.get(xmKey);
    if (Objects.isNull(project)) return ApiResult.badRequest("invalid xmkey");
    LocalDate time = date.orElse(LocalDate.now().minusDays(1));
    Set<Kpi> kpis = new HashSet<>();
    String kpiStr;
    for (Kpi kpi : project.getKpis())
      if (StringUtils.isNotBlank(kpiStr = request.getParameter(kpi.getKpiId().toString())))
        kpis.add(new Kpi(xmId, kpi.getKpiId(), new BigDecimal(kpiStr), time));
    return kpiManager.addOrUpdate(kpis);
  }

  @ApiMethod(description = "select projects do not submit kpi on named date")
  @RequestMapping(value = "/kpi/projects", method = RequestMethod.GET)
  public ApiResult<?> add(
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd", required = false) @RequestParam @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> date) {
    return kpiManager.selectProjectsDoNotSubmitKpiOnNamedDate(date.orElse(LocalDate.now().minusDays(1)));
  }
}
