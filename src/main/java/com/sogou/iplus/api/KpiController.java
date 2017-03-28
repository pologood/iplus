/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.api;

import static com.sogou.iplus.entity.Project.getProjectWithXmId;
import static com.sogou.iplus.entity.Project.getProjectWithXmIdAndXmKey;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.validator.constraints.NotEmpty;
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

import com.google.common.collect.ImmutableMap;
import com.sogou.iplus.entity.Company;
import com.sogou.iplus.entity.Kpi;
import com.sogou.iplus.entity.Project;
import com.sogou.iplus.manager.KpiManager;
import com.sogou.iplus.manager.PermissionManager;
import com.sogou.iplus.manager.PermissionManager.Role;
import com.sogou.iplus.manager.PushManager;
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

  @Autowired
  private PermissionManager permissionManager;

  @Autowired
  private PushManager pushManager;

  @ApiMethod(description = "update kpi record")
  @RequestMapping(value = "/kpi", method = RequestMethod.PUT)
  public ApiResult<?> update(HttpServletRequest request,
      @ApiQueryParam(name = "xmId", description = "项目id") @RequestParam int xmId,
      @ApiQueryParam(name = "xmKey", description = "项目秘钥") @RequestParam String xmKey,
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
    Project project = getProjectWithXmIdAndXmKey(xmId, xmKey);
    if (Objects.isNull(project)) return ApiResult.forbidden();
    Set<Kpi> kpis = new HashSet<>();
    String kpiStr;
    for (Integer kpiId : project.getKpiList())
      if (NumberUtils.isNumber(kpiStr = request.getParameter(kpiId.toString())))
        kpis.add(new Kpi(xmId, kpiId, new BigDecimal(kpiStr), date));
    return kpiManager.update(kpis);
  }

  @ApiMethod(description = "select projects do not submit kpi on named date")
  @RequestMapping(value = "/kpi/null", method = RequestMethod.GET)
  public ApiResult<?> selectProjectsDoNotSubmitKpiOnNamedDate(
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> date) {
    return kpiManager.selectProjectsDoNotSubmitKpiOnNamedDate(date.orElse(LocalDate.now()));
  }

  @ApiMethod(description = "select kpis with date range and kpiId")
  @RequestMapping(value = "/kpi/range", method = RequestMethod.GET)
  public ApiResult<?> selectKpisWithDateRangeAndKpiId(@RequestParam(defaultValue = "0") int from,
      @AuthenticationPrincipal User user,
      @ApiQueryParam(name = "xmId", description = "项目Id") @RequestParam Optional<Integer> xmId,
      @ApiQueryParam(name = "xmKey", description = "项目秘钥") @RequestParam Optional<String> xmKey,
      @ApiQueryParam(name = "kpiId", description = "kpiId") @RequestParam @NotEmpty List<Integer> kpiId,
      @ApiQueryParam(name = "beginDate", description = "起始日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate beginDate,
      @ApiQueryParam(name = "endDate", description = "结束日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
    if (!isValid(from, user, xmId, xmKey, kpiId)) return ApiResult.forbidden();
    return kpiManager.selectWithDateRangeAndKpiId(xmId.orElse(null), kpiId, beginDate, endDate);
  }

  private boolean isValid(int from, User user, Optional<Integer> xmId, Optional<String> xmKey, List<Integer> kpiIds) {
    return permissionManager.isAuthorized(user, kpiIds)
        || (Objects.equals(from, HOST.privateWeb.getValue()) && isValid(xmId, xmKey, kpiIds));
  }

  @ApiMethod(description = "get company structure information")
  @RequestMapping(value = "/company", method = RequestMethod.GET)
  public ApiResult<?> getCompany(HttpServletResponse response, @AuthenticationPrincipal User user) {
    return new ApiResult<>(permissionManager.getCompany(permissionManager.getValidKpiIdsFromUser(user)));
  }

  @ApiMethod(description = "list company kpi")
  @RequestMapping(value = "/company/kpi", method = RequestMethod.GET)
  public ApiResult<?> getCompanyKpis() {
    Set<Integer> kpis = new HashSet<>();
    kpis.addAll(Company.SOGOU.getKpis());
    Company.SOGOU.getBusinessUnits().forEach(bu -> kpis.addAll(bu.getKpis()));
    return new ApiResult<>(kpis.stream().sorted().map(Project::getKpi).collect(Collectors.toList()));
  }

  @ApiMethod(description = "select kpis with xmId on named date")
  @RequestMapping(value = "/kpi", method = RequestMethod.GET)
  public ApiResult<?> selectKpisWithDateAndXmId(@RequestParam(defaultValue = "0") int from,
      @AuthenticationPrincipal User user,
      @ApiQueryParam(name = "xmId", description = "项目Id") @RequestParam Optional<Integer> xmId,
      @ApiQueryParam(name = "xmKey", description = "项目秘钥") @RequestParam Optional<String> xmKey,
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
    Project project = getProjectWithXmId(xmId.orElse(null));
    List<Integer> kpiIds = Objects.isNull(project) ? new ArrayList<>() : project.getKpiList();
    if (!isValid(from, user, xmId, xmKey, kpiIds)) return ApiResult.forbidden();
    return kpiManager.selectWithDateAndXmId(xmId.orElse(null), date);
  }

  private boolean isValid(Optional<Integer> xmId, Optional<String> xmKey, List<Integer> kpiId) {
    if (!xmId.isPresent() || !xmKey.isPresent()) return false;
    return isValid(xmId.get(), xmKey.get(), kpiId);
  }

  private boolean isValid(int xmId, String xmKey, List<Integer> kpiIds) {
    Project project = getProjectWithXmIdAndXmKey(xmId, xmKey);
    return Objects.nonNull(project) && (xmId == 0 || project.getKpiList().containsAll(kpiIds));
  }

  @ApiMethod(description = "add kpi record")
  @RequestMapping(value = "/kpi", method = RequestMethod.POST)
  public ApiResult<?> add(
      @ApiQueryParam(name = "date", description = "上传日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) Optional<LocalDate> date) {
    return kpiManager.addAll(date.orElse(LocalDate.now()));
  }

  @ApiMethod(description = "push pandora message")
  @RequestMapping(value = "/kpi/message", method = RequestMethod.POST)
  public ApiResult<?> pushPandoraMessage(
      @ApiQueryParam(name = "role", description = "发送对象角色") @RequestParam Optional<List<Role>> role) {
    return pushManager.push(new HashSet<>(role.orElse(Arrays.asList(Role.ADMIN))));
  }

  @ApiMethod(description = "get average kpi")
  @RequestMapping(value = "/kpi/average", method = RequestMethod.GET)
  public ApiResult<?> getAverage(@ApiQueryParam(name = "xmId", description = "项目Id") @RequestParam int xmId,
      @ApiQueryParam(name = "xmKey", description = "项目秘钥") @RequestParam String xmKey,
      @ApiQueryParam(name = "kpiIds", description = "kpiId list") @RequestParam Optional<List<String>> kpiIds,
      @ApiQueryParam(name = "date", description = "kpi日期", format = "yyyy-MM-dd") @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date,
      @ApiQueryParam(name = "type", description = "平均时间范围类型") @RequestParam AVERAGE type) {
    List<Integer> kpiIdList = splitKpiIds(kpiIds.orElse(new ArrayList<>()));
    if (!isValid(xmId, xmKey, kpiIdList)) return ApiResult.forbidden();
    return kpiManager.getAverage(kpiIdList, xmId, kpiIds.orElse(new ArrayList<>()),
        date.minusDays(AVERAGE_MAP.get(type) - 1), date);
  }

  List<Integer> splitKpiIds(List<String> kpiIds) {
    return kpiIds.stream().flatMap(kpiId -> Arrays.stream(kpiId.split("\\+"))).map(Integer::parseInt)
        .collect(Collectors.toList());
  }

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

  public enum AVERAGE {
    day, week, month;
  }

  private Map<AVERAGE, Integer> AVERAGE_MAP = ImmutableMap.of(AVERAGE.day, 1, AVERAGE.week, 7, AVERAGE.month, 30);
}
