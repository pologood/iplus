/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package iplus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sogou.iplus.api.KpiController;
import com.sogou.iplus.api.KpiController.HOST;
import com.sogou.iplus.config.DaoConfig;
import com.sogou.iplus.config.RootConfig;
import com.sogou.iplus.entity.Company;
import com.sogou.iplus.entity.Project;
import com.sogou.iplus.model.ApiResult;

import commons.spring.RedisRememberMeService.User;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月14日;
//-------------------------------------------------------
@ContextConfiguration(classes = { RootConfig.class, DaoConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class KpiControllerTest {

  @Mock
  private HttpServletRequest request;

  private final int testId = 70, debugId = 0;

  private final Project testProject = Project.PROJECT_MAP.get(testId), debugProject = Project.PROJECT_MAP.get(debugId);

  private final String testKey = testProject.getXmKey(), debugKey = debugProject.getXmKey();

  private final List<Integer> kpiIds = testProject.getKpis().stream().map(kpi -> kpi.getKpiId())
      .collect(Collectors.toList());

  private LocalDate today = LocalDate.now(), yesterday = today.minusDays(1), tomorrow = today.plusDays(1);

  @Autowired
  private KpiController controller;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    testProject.getKpis().forEach(
        kpi -> Mockito.when(request.getParameter(kpi.getKpiId().toString())).thenReturn(kpi.getKpiId().toString()));
  }

  @Test
  public void test() {
    Assert.assertNotNull(controller);
    getCompany();
    add();
    update();
    selectNull();
    selectKpisWithDateAndXmId();
    selectKpisWithDateRangeAndKpiId();
    push();
    System.out.println(getRandomString("0123456789abcdefghijklmnopqrstuvwxyz".toCharArray(), 16));
  }

  public void getCompany() {
    ApiResult<?> result = controller.getCompany(null, new User("xiaop_liteng", "李腾"), Optional.empty());
    Assert.assertTrue(ApiResult.isOk(result));
    Company sogou = (Company) result.getData();
    Assert.assertNotNull(sogou);
    System.out.println(sogou);
    Assert.assertFalse(CollectionUtils.isEmpty(sogou.getBusinessUnits()));
  }

  public void add() {
    Assert.assertTrue(ApiResult.isOk(controller.add(Optional.empty())));
  }

  public void update() {
    Assert.assertTrue(ApiResult.isOk(controller.update(request, testId, testKey, yesterday)));
  }

  public void selectNull() {
    ApiResult<?> result = controller.selectProjectsDoNotSubmitKpiOnNamedDate(Optional.empty());
    Assert.assertTrue(ApiResult.isOk(result));
    List<?> list = (List<?>) result.getData();
    Assert.assertFalse(CollectionUtils.isEmpty(list));
    Assert.assertTrue(list.stream().map(o -> (Project) o).noneMatch(p -> Objects.equals(testId, p.getXmId())));
  }

  public void selectKpisWithDateAndXmId() {
    validateResultOfSelectKpisWithDateAndXmId(selectKpisWithDateAndXmId(testId, testKey));
    validateResultOfSelectKpisWithDateAndXmId(selectKpisWithDateAndXmId(debugId, debugKey));
  }

  void validateResultOfSelectKpisWithDateAndXmId(ApiResult<?> result) {
    Assert.assertTrue(ApiResult.isOk(result));
    Map<?, ?> map = (Map<?, ?>) result.getData();
    Assert.assertFalse(MapUtils.isEmpty(map));
    System.out.println(map);
    Assert.assertTrue(map.entrySet().stream().filter(e -> kpiIds.contains(e.getKey()))
        .allMatch(e -> Objects.equals((Integer) e.getKey(), ((BigDecimal) e.getValue()).intValue())));
  }

  private ApiResult<?> selectKpisWithDateAndXmId(int xmId, String xmKey) {
    return controller.selectKpisWithDateAndXmId(HOST.privateWeb.getValue(), null, null, Optional.empty(),
        Optional.of(xmId), Optional.of(xmKey), today);
  }

  public void selectKpisWithDateRangeAndKpiId() {
    validateResultOfSelectKpisWithDateRangeAndKpiId(
        selectKpisWithDateRangeAndKpiId(HOST.privateWeb, null, testId, testKey));
    User user = new User("xiaop_liteng", "李腾");
    validateResultOfSelectKpisWithDateRangeAndKpiId(selectKpisWithDateRangeAndKpiId(HOST.publicWeb, user, null, null));
  }

  private ApiResult<?> selectKpisWithDateRangeAndKpiId(HOST host, User user, Integer xmId, String xmKey) {
    return controller.selectKpisWithDateRangeAndKpiId(host.getValue(), null, user, Optional.empty(),
        Optional.ofNullable(xmId), Optional.ofNullable(xmKey), kpiIds, today, tomorrow);
  }

  private void validateResultOfSelectKpisWithDateRangeAndKpiId(ApiResult<?> result) {
    Assert.assertTrue(ApiResult.isOk(result));
    Map<?, ?> map = (Map<?, ?>) result.getData();
    Assert.assertTrue(MapUtils.isNotEmpty(map) && map.entrySet().stream().allMatch(e -> kpiIds.contains(e.getKey())));
    System.out.println(map);
  }

  public void push() {
    controller.pushPandoraMessage(Optional.empty());
  }

  public String getRandomString(char[] chars, int len) {
    Random random = new Random();
    char[] result = new char[len];
    for (int i = 0; i < len; result[i++] = chars[random.nextInt(chars.length)]);
    return new String(result);
  }
}
