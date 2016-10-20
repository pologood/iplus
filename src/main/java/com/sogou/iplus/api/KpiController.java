/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sogou.iplus.entity.Kpi;
import com.sogou.iplus.entity.Project;
import com.sogou.iplus.manager.KpiManager;
import com.sogou.iplus.model.ApiResult;

import commons.saas.LoginService.User;

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
    Project project = Project.PROJECT_MAP.get(xmId);
    if (Objects.isNull(project)) return ApiResult.badRequest("invalid xmId");
    if (!Objects.equals(project.getProjectKey(), xmKey)) return ApiResult.forbidden();
    LocalDate time = date.orElse(LocalDate.now().minusDays(1));
    Set<Kpi> kpis = new HashSet<>();
    String kpiStr;
    for (Kpi kpi : project.getKpis())
      if (StringUtils.isNotBlank(kpiStr = request.getParameter(kpi.getKpiId().toString())))
        kpis.add(new Kpi(xmId, kpi.getKpiId(), new BigDecimal(kpiStr), time));
    return kpiManager.addOrUpdate(kpis);
  }

  @ApiMethod(description = "select projects do not submit kpi on named date")
  @RequestMapping(value = "/kpi/null", method = RequestMethod.GET)
  public ApiResult<?> selectProjectsDoNotSubmitKpiOnNamedDate(
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd", required = false) @RequestParam @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> date) {
    return kpiManager.selectProjectsDoNotSubmitKpiOnNamedDate(date.orElse(LocalDate.now().minusDays(1)));
  }

  @ApiMethod(description = "select kpis")
  @RequestMapping(value = "/kpi", method = RequestMethod.GET)
  public ApiResult<?> selectKpisWithDateAndProjectId(
      @ApiQueryParam(name = "projectId", description = "项目Id", required = false) @RequestParam Optional<Integer> projectId,
      @ApiQueryParam(name = "beginDate", description = "起始日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate beginDate,
      @ApiQueryParam(name = "endDate", description = "结束日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
    return kpiManager.selectKpisWithDateAndProjectId(projectId, beginDate, endDate);
  }

  @ApiMethod(description = "list projects")
  @RequestMapping(value = "/project", method = RequestMethod.GET)
  public ApiResult<?> listProjects() {
    Map<String, Set<Project>> map = new HashMap<>();
    Project.PROJECTS.stream().filter(p -> Objects.nonNull(p.getBusinessUnit()))
        .forEach(p -> map.computeIfAbsent(p.getBusinessUnit().getValue(), k -> new HashSet<>()).add(p));
    return new ApiResult<>(map);
  }

  @ApiMethod(description = "list kpis")
  @RequestMapping(value = "/project/kpi", method = RequestMethod.GET)
  public ApiResult<?> listKpis(@ApiQueryParam(name = "projectId", description = "项目Id") @RequestParam int projectId) {
    return new ApiResult<>(Project.PROJECT_MAP.get(projectId).getKpis());
  }

  @ApiMethod(description = "select kpis on named date")
  @RequestMapping(value = "/kpi/project", method = RequestMethod.GET)
  public ApiResult<?> selectKpisWithDateAndProjectId(
      @RequestAttribute(name = CookieInterceptor.ATTRIBUTE_NAME, required = false) User user,
      @ApiQueryParam(name = "projectId", description = "项目Id") @RequestParam int projectId,
      @ApiQueryParam(name = "projectKey", description = "项目秘钥") @RequestParam Optional<String> projectKey,
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
    if (Objects.isNull(user) && !isDebugProjectId(projectId) && !isValidKey(projectId, projectKey))
      return ApiResult.forbidden();
    return kpiManager.selectKpisWithDateAndProjectId(projectId, date);
  }

  private boolean isDebugProjectId(int projectId) {
    return projectId == 0;
  }

  private boolean isValidKey(int projectId, Optional<String> projectKey) {
    if (!projectKey.isPresent()) return false;
    Project project = Project.PROJECT_MAP.get(projectId);
    if (Objects.isNull(project)) return false;
    return Objects.equals(project.getProjectKey(), projectKey.get());
  }
}
