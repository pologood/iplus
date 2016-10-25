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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import commons.saas.XiaopLoginService;
import commons.spring.RedisRememberMeService;
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

  @Autowired
  RedisRememberMeService redisService;

  @Autowired
  XiaopLoginService pandoraService;

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

  private Project getProject(int xmId, String xmKey) {
    Project project = Project.PROJECT_MAP.get(xmId);
    return Objects.nonNull(project) && Objects.equals(xmKey, project.getXmKey()) ? project : null;
  }

  @ApiMethod(description = "select projects do not submit kpi on named date")
  @RequestMapping(value = "/kpi/null", method = RequestMethod.GET)
  public ApiResult<?> selectProjectsDoNotSubmitKpiOnNamedDate(
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
    return kpiManager.selectProjectsDoNotSubmitKpiOnNamedDate(date);
  }

  @ApiMethod(description = "select kpis with date range and kpiId")
  @RequestMapping(value = "/kpi/range", method = RequestMethod.GET)
  public ApiResult<?> selectKpisWithDateRangeAndKpiId(@AuthenticationPrincipal User user, HttpServletResponse response,
      @ApiQueryParam(name = "token", description = "pandora token") @RequestParam Optional<String> token,
      @ApiQueryParam(name = "xmId", description = "项目Id") @RequestParam Optional<Integer> xmId,
      @ApiQueryParam(name = "xmKey", description = "项目秘钥") @RequestParam Optional<String> xmKey,
      @ApiQueryParam(name = "kpiId", description = "kpiId") @RequestParam int kpiId,
      @ApiQueryParam(name = "beginDate", description = "起始日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate beginDate,
      @ApiQueryParam(name = "endDate", description = "结束日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
    if (!isValid(user, token, xmId, xmKey, response, kpiId)) return ApiResult.forbidden();
    return kpiManager.selectWithDateRangeAndKpiId(xmId.orElse(null), kpiId, beginDate, endDate);
  }

  private boolean isValid(User user, Optional<String> token, Optional<Integer> xmId, Optional<String> xmKey,
      HttpServletResponse response, Integer kpiId) {
    return isValid(user, kpiId) || isValid(xmId, xmKey, kpiId) || login(token, response, kpiId);
  }

  private boolean isValid(User user, Integer kpiId) {
    return Objects.nonNull(user);
  }

  private boolean login(Optional<String> token, HttpServletResponse response, Integer kpiId) {
    if (!token.isPresent()) return false;
    commons.saas.LoginService.User user = pandoraService.login(token.get());
    if (Objects.isNull(user)) return false;
    User user2 = new User(user.getOpenId(), user.getName());
    redisService.login(response, user2);
    return isValid(user2, kpiId);
  }

  @ApiMethod(description = "get company structure information")
  @RequestMapping(value = "/company", method = RequestMethod.GET)
  public ApiResult<?> getCompany() {
    return new ApiResult<>(Company.SOGOU);
  }

  @ApiMethod(description = "select kpis with xmId on named date")
  @RequestMapping(value = "/kpi", method = RequestMethod.GET)
  public ApiResult<?> selectKpisWithDateAndXmId(HttpServletResponse response, @AuthenticationPrincipal User user,
      @ApiQueryParam(name = "token", description = "pandora token") @RequestParam Optional<String> token,
      @ApiQueryParam(name = "xmId", description = "项目Id") @RequestParam Optional<Integer> xmId,
      @ApiQueryParam(name = "xmKey", description = "项目秘钥") @RequestParam Optional<String> xmKey,
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
    if (!isValid(user, token, xmId, xmKey, response, null)) return ApiResult.forbidden();
    return kpiManager.selectWithDateAndXmId(xmId.orElse(null), date);
  }

  private boolean isValid(Optional<Integer> xmId, Optional<String> xmKey, Integer kpiId) {
    if (!xmId.isPresent() || !xmKey.isPresent()) return false;
    Project project = Project.PROJECT_MAP.get(xmId.get());
    return Objects.nonNull(project) && Objects.equals(project.getXmKey(), xmKey.get())
        && (Objects.equals(xmId.get(), 0) || Objects.isNull(kpiId) ? true
            : project.getKpis().stream().map(kpi -> kpi.getKpiId()).collect(Collectors.toSet()).contains(kpiId));
  }

  @ApiMethod(description = "add kpi record")
  @RequestMapping(value = "/kpi", method = RequestMethod.POST)
  public ApiResult<?> add(@ApiQueryParam(name = "date", description = "上传日期") @RequestParam Optional<LocalDate> date) {
    return kpiManager.addAll(date.orElse(LocalDate.now()));
  }
}
