/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sogou.iplus.entity.Company;
import com.sogou.iplus.entity.Kpi;
import com.sogou.iplus.entity.Project;
import com.sogou.iplus.manager.KpiManager;
import com.sogou.iplus.model.ApiResult;

import commons.spring.RedisRememberMeService.User;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月11日;
//-------------------------------------------------------
@Api(name = "kpi API", description = "Read/Write/Update/Delete the kpi")
@RestController
@RequestMapping("/api")
public class KpiController {

  @Autowired
  private KpiManager kpiManager;

  @ApiMethod(description = "update kpi record")
  @RequestMapping(value = "/kpi", method = RequestMethod.PUT)
  public ApiResult<?> update(HttpServletRequest request,
      @ApiQueryParam(name = "xmId", description = "项目id") @RequestParam int xmId,
      @ApiQueryParam(name = "xmKey", description = "项目秘钥") @RequestParam String xmKey,
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
    Project project = getProject(xmId, xmKey);
    if (Objects.isNull(project)) return ApiResult.forbidden();
    Set<Kpi> kpis = new HashSet<>();
    String kpiStr;
    for (Kpi kpi : project.getKpis())
      if (StringUtils.isNotBlank(kpiStr = request.getParameter(kpi.getKpiId().toString())))
        kpis.add(new Kpi(xmId, kpi.getKpiId(), new BigDecimal(kpiStr), date));
    return kpiManager.update(kpis);
  }

  @ApiMethod(description = "select projects do not submit kpi on named date")
  @RequestMapping(value = "/kpi/null", method = RequestMethod.GET)
  public ApiResult<?> selectProjectsDoNotSubmitKpiOnNamedDate(
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
    return kpiManager.selectProjectsDoNotSubmitKpiOnNamedDate(date);
  }

  @ApiMethod(description = "select kpis with date range and kpiId")
  @RequestMapping(value = "/kpi/range", method = RequestMethod.GET)
  public ApiResult<?> selectKpisWithDateRangeAndKpiId(
      @ApiQueryParam(name = "xmId", description = "项目Id") @RequestParam int xmId,
      @ApiQueryParam(name = "xmKey", description = "项目秘钥") @RequestParam String xmKey,
      @ApiQueryParam(name = "kpiId", description = "kpiId") @RequestParam int kpiId,
      @ApiQueryParam(name = "beginDate", description = "起始日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate beginDate,
      @ApiQueryParam(name = "endDate", description = "结束日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
    Project project = getProject(xmId, xmKey);
    if (Objects.isNull(project) || (xmId != 0
        && !project.getKpis().stream().map(kpi -> kpi.getKpiId()).collect(Collectors.toSet()).contains(kpiId)))
      return ApiResult.forbidden();
    return kpiManager.selectWithDateRangeAndKpiId(xmId, kpiId, beginDate, endDate);
  }

  @ApiMethod(description = "get company structure information")
  @RequestMapping(value = "/company", method = RequestMethod.GET)
  public ApiResult<?> getCompany() {
    return new ApiResult<>(Company.SOGOU);
  }

  @ApiMethod(description = "select kpis with xmId on named date")
  @RequestMapping(value = "/kpi", method = RequestMethod.GET)
  public ApiResult<?> selectKpisWithDateAndXmId(@AuthenticationPrincipal User user,
      @ApiQueryParam(name = "xmId", description = "项目Id") @RequestParam int xmId,
      @ApiQueryParam(name = "xmKey", description = "项目秘钥") @RequestParam String xmKey,
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
    if (Objects.isNull(user) && Objects.isNull(getProject(xmId, xmKey))) return ApiResult.forbidden();
    return kpiManager.selectWithDateAndXmId(xmId, date);
  }

  private Project getProject(int xmId, String xmKey) {
    Project project = Project.PROJECT_MAP.get(xmId);
    return Objects.isNull(project) || !Objects.equals(project.getXmKey(), xmKey) ? null : project;
  }

  @ApiMethod(description = "add kpi record")
  @RequestMapping(value = "/kpi", method = RequestMethod.POST)
  public ApiResult<?> add() {
    return kpiManager.addAll();
  }
}
