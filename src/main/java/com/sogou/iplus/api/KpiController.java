/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.api;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sogou.iplus.entity.BusinessUnit;
import com.sogou.iplus.entity.Company;
import com.sogou.iplus.entity.Kpi;
import com.sogou.iplus.entity.Project;
import com.sogou.iplus.manager.KpiManager;
import com.sogou.iplus.model.ApiResult;

import commons.saas.XiaopLoginService;
import commons.saas.XiaopService;
import commons.saas.XiaopService.PushParam;
import commons.spring.RedisRememberMeService;
import commons.spring.RedisRememberMeService.User;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月11日;
//-------------------------------------------------------
@Api(name = "kpi API", description = "Read/Write/Update/Delete the kpi")
@RestController
@RequestMapping("/api")
public class KpiController implements InitializingBean {

  @Autowired
  Environment env;

  @Autowired
  private KpiManager kpiManager;

  @Autowired
  RedisRememberMeService redisService;

  @Autowired
  XiaopLoginService pandoraLoginService;

  @Autowired
  XiaopService pandoraService;

  private Set<String> whiteList = new HashSet<>();

  @ApiMethod(description = "update kpi record")
  @RequestMapping(value = "/kpi", method = RequestMethod.PUT)
  public ApiResult<?> update(HttpServletRequest request,
      @ApiQueryParam(name = "xmId", description = "项目id") @RequestParam(defaultValue = "0") int xmId,
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
  public ApiResult<?> selectKpisWithDateRangeAndKpiId(@RequestParam(defaultValue = "0") int from,
      HttpServletResponse response, @AuthenticationPrincipal User user,
      @ApiQueryParam(name = "token", description = "pandora token") @RequestParam Optional<String> token,
      @ApiQueryParam(name = "xmId", description = "项目Id") @RequestParam Optional<Integer> xmId,
      @ApiQueryParam(name = "xmKey", description = "项目秘钥") @RequestParam Optional<String> xmKey,
      @ApiQueryParam(name = "kpiId", description = "kpiId") @RequestParam @NotEmpty List<Integer> kpiId,
      @ApiQueryParam(name = "beginDate", description = "起始日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate beginDate,
      @ApiQueryParam(name = "endDate", description = "结束日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
    if (!isValid(from, user, token, xmId, xmKey, response, kpiId)) return ApiResult.forbidden();
    return kpiManager.selectWithDateRangeAndKpiId(xmId.orElse(null), kpiId, beginDate, endDate);
  }

  private boolean isValid(int from, User user, Optional<String> token, Optional<Integer> xmId, Optional<String> xmKey,
      HttpServletResponse response, List<Integer> kpiId) {
    return isValid(xmId, xmKey, kpiId) || isValid(user, kpiId) || login(token, response, kpiId);
  }

  private boolean isValid(User user, List<Integer> kpiId) {
    return Objects.nonNull(user) && isAuthorized(user, kpiId);
  }

  private boolean isAuthorized(User user, List<Integer> kpiId) {
    return whiteList.contains(user.getId());
  }

  private boolean login(Optional<String> token, HttpServletResponse response, List<Integer> kpiId) {
    if (!token.isPresent()) return false;
    commons.saas.LoginService.User user = pandoraLoginService.login(token.get());
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

  @ApiMethod(description = "list company kpi")
  @RequestMapping(value = "/company/kpi", method = RequestMethod.GET)
  public ApiResult<?> getCompanyKpis() {
    Set<Integer> kpis = new HashSet<>();
    kpis.addAll(Company.SOGOU.getKpis());
    kpis.addAll(BusinessUnit.DESKTOP.getKpis());
    kpis.addAll(BusinessUnit.SEARCH.getKpis());
    return new ApiResult<>(kpis.stream().sorted().map(id -> Project.getKpi(id)).collect(Collectors.toList()));
  }

  @ApiMethod(description = "select kpis with xmId on named date")
  @RequestMapping(value = "/kpi", method = RequestMethod.GET)
  public ApiResult<?> selectKpisWithDateAndXmId(@RequestParam int from, HttpServletResponse response,
      @AuthenticationPrincipal User user,
      @ApiQueryParam(name = "token", description = "pandora token") @RequestParam Optional<String> token,
      @ApiQueryParam(name = "xmId", description = "项目Id") @RequestParam Optional<Integer> xmId,
      @ApiQueryParam(name = "xmKey", description = "项目秘钥") @RequestParam Optional<String> xmKey,
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
    if (!isValid(from, user, token, xmId, xmKey, response, null)) return ApiResult.forbidden();
    return kpiManager.selectWithDateAndXmId(xmId.orElse(null), date);
  }

  private boolean isValid(Optional<Integer> xmId, Optional<String> xmKey, List<Integer> kpiId) {
    if (!xmId.isPresent() || !xmKey.isPresent()) return false;
    Project project = Project.PROJECT_MAP.get(xmId.get());
    return Objects.nonNull(project) && Objects.equals(project.getXmKey(), xmKey.get())
        && (Objects.equals(xmId.get(), 0) || Objects.isNull(kpiId) ? true
            : project.getKpis().stream().map(kpi -> kpi.getKpiId()).collect(Collectors.toSet()).containsAll(kpiId));
  }

  @ApiMethod(description = "add kpi record")
  @RequestMapping(value = "/kpi", method = RequestMethod.POST)
  public ApiResult<?> add(
      @ApiQueryParam(name = "date", description = "上传日期") @RequestParam @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> date) {
    return kpiManager.addAll(date.orElse(LocalDate.now()));
  }

  @ApiMethod(description = "push pandora message")
  @RequestMapping(value = "/kpi/message", method = RequestMethod.POST)
  public ApiResult<?> pushPandoraMessage() {
    PushParam param = new PushParam();
    param.setImage(COVER);
    param.setMessage(MESSAGE);
    param.setOpenId(EMAIL_LIST);
    param.setTitle("[" + LocalDate.now() + "]" + TITLE);
    param.setUrl(URL);
    String result = pandoraService.push(param);
    return Objects.isNull(result) ? ApiResult.ok() : ApiResult.internalError(result);
  }

  private String PUBLIC_ID, COVER, MESSAGE = "今日搜狗业务指标已更新，请点击查看", EMAIL_LIST, TITLE = "搜狗业务指标", TOKEN, URL, BOSS;

  public enum HOST {
    publicWeb(0), privateWeb(1);

    private int value;

    private HOST(int val) {
      this.value = val;
    }

    public int getValue() {
      return this.value;
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    PUBLIC_ID = env.getRequiredProperty("pandora.message.publicid");
    COVER = env.getRequiredProperty("pandora.message.cover");
    EMAIL_LIST = env.getRequiredProperty("pandora.message.list");
    TOKEN = env.getRequiredProperty("pandora.message.token");
    URL = env.getRequiredProperty("pandora.message.url");
    BOSS = env.getProperty("boss", "wxc,ruliyun,yanghongtao,hongtao");

    pandoraService.setAppId(PUBLIC_ID);
    pandoraService.setAppKey(TOKEN);

    Arrays.asList(EMAIL_LIST, BOSS)
        .forEach(s -> Arrays.stream(s.split(",")).map(user -> "xiaop_" + user).forEach(user -> whiteList.add(user)));
  }
}
