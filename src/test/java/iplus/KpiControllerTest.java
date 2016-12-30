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

  private final int xmId = 70, debugId = 0;

  private final Project project = Project.PROJECT_MAP.get(xmId), debugProject = Project.PROJECT_MAP.get(debugId);

  private final String xmKey = project.getXmKey(), debugKey = debugProject.getXmKey();

  private final List<Integer> kpiId = project.getKpis().stream().map(kpi -> kpi.getKpiId())
      .collect(Collectors.toList());

  private LocalDate today = LocalDate.now(), yesterday = today.minusDays(1), tomorrow = today.plusDays(1);

  @Autowired
  private KpiController controller;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    project.getKpis().forEach(
        kpi -> Mockito.when(request.getParameter(kpi.getKpiId().toString())).thenReturn(kpi.getKpiId().toString()));
  }

  @Test
  public void test() {
    //push();
    Assert.assertNotNull(controller);
    getCompany();
    add();
    update();
    selectNull();
    selectKpisWithDateAndXmId();
    selectKpisWithDateRangeAndKpiId();
  }

  public void getCompany() {
    ApiResult<?> result = controller.getCompany(null, null, Optional.empty());
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
    Assert.assertTrue(ApiResult.isOk(controller.update(request, xmId, xmKey, yesterday)));
  }

  public void selectNull() {
    ApiResult<?> result = controller.selectProjectsDoNotSubmitKpiOnNamedDate(Optional.of(today));
    Assert.assertTrue(ApiResult.isOk(result));
    List<?> list = (List<?>) result.getData();
    Assert.assertFalse(CollectionUtils.isEmpty(list));
    Assert.assertFalse(list.stream().map(o -> (Project) o).anyMatch(p -> Objects.equals(xmId, p.getXmId())));
  }

  public void selectKpisWithDateAndXmId() {
    validateResultOfSelectKpisWithDateAndXmId(selectKpisWithDateAndXmId(xmId, xmKey));
    validateResultOfSelectKpisWithDateAndXmId(selectKpisWithDateAndXmId(debugId, debugKey));
  }

  void validateResultOfSelectKpisWithDateAndXmId(ApiResult<?> result) {
    Assert.assertTrue(ApiResult.isOk(result));
    Map<?, ?> map = (Map<?, ?>) result.getData();
    Assert.assertFalse(MapUtils.isEmpty(map));
    System.out.println(map);
    Assert.assertTrue(map.entrySet().stream().filter(e -> kpiId.contains(e.getKey()))
        .allMatch(e -> Objects.equals((Integer) e.getKey(), ((BigDecimal) e.getValue()).intValue())));
  }

  private ApiResult<?> selectKpisWithDateAndXmId(int xmId, String xmKey) {
    return controller.selectKpisWithDateAndXmId(HOST.privateWeb.getValue(), null, null, Optional.empty(),
        Optional.of(xmId), Optional.of(xmKey), today);
  }

  public void selectKpisWithDateRangeAndKpiId() {
    validateResultOfSelectKpisWithDateRangeAndKpiId(
        selectKpisWithDateRangeAndKpiId(HOST.privateWeb, null, xmId, xmKey));
    User user = new User("xiaop_lisihao", "李思昊");
    validateResultOfSelectKpisWithDateRangeAndKpiId(selectKpisWithDateRangeAndKpiId(HOST.publicWeb, user, null, null));
  }

  private ApiResult<?> selectKpisWithDateRangeAndKpiId(HOST host, User user, Integer xmId, String xmKey) {
    return controller.selectKpisWithDateRangeAndKpiId(host.getValue(), null, user, Optional.empty(),
        Optional.ofNullable(xmId), Optional.ofNullable(xmKey), kpiId, today, tomorrow);
  }

  private void validateResultOfSelectKpisWithDateRangeAndKpiId(ApiResult<?> result) {
    Assert.assertTrue(ApiResult.isOk(result));
    Map<?, ?> map = (Map<?, ?>) result.getData();
    Assert.assertTrue(MapUtils.isNotEmpty(map)
        && map.entrySet().stream().filter(e -> kpiId.contains(e.getKey())).findAny().isPresent());
    System.out.println(map);
  }

  public void push() {
    controller.pushPandoraMessage(Optional.empty());
  }
}
