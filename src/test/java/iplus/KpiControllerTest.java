/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package iplus;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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
import com.sogou.iplus.config.DaoConfig;
import com.sogou.iplus.config.RootConfig;
import com.sogou.iplus.entity.Company;
import com.sogou.iplus.entity.Project;
import com.sogou.iplus.model.ApiResult;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月14日;
//-------------------------------------------------------
@ContextConfiguration(classes = { RootConfig.class, DaoConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class KpiControllerTest {

  @Mock
  private HttpServletRequest mockRequest;

  private final int testXmId = 70;

  private final Project testProject = Project.PROJECT_MAP.get(testXmId);

  private final String testXmKey = testProject.getXmKey();

  private final int testKpiId = testProject.getKpis().iterator().next().getKpiId();

  @Autowired
  private KpiController controller;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void test() {
    getCompany();
    add();
    update();
    selectNull();
    selectKpisWithDateAndXmId();
    selectKpisWithDateRangeAndKpiId();
  }

  public void getCompany() {
    ApiResult<?> result = controller.getCompany();
    Assert.assertTrue(ApiResult.isOk(result));
    Company sogou = (Company) result.getData();
    System.out.println(sogou);
    Assert.assertFalse(sogou.getBusinessUnits().isEmpty());
  }

  public void add() {
    Assert.assertTrue(ApiResult.isOk(controller.add()));
  }

  public void update() {
    testProject.getKpis().forEach(
        kpi -> Mockito.when(mockRequest.getParameter(kpi.getKpiId().toString())).thenReturn(kpi.getKpiId().toString()));
    Assert
        .assertTrue(ApiResult.isOk(controller.update(mockRequest, testXmId, testXmKey, LocalDate.now().minusDays(1))));
  }

  public void selectNull() {
    ApiResult<?> result = controller.selectProjectsDoNotSubmitKpiOnNamedDate(LocalDate.now());
    Assert.assertTrue(ApiResult.isOk(result));
    List<?> objects = (List<?>) result.getData();
    Assert.assertFalse(objects.isEmpty());
    Assert.assertEquals(0,
        objects.stream().map(o -> (Project) o).filter(project -> Objects.equals(testXmId, project.getXmId())).count());
  }

  public void selectKpisWithDateAndXmId() {
    ApiResult<?> result = controller.selectKpisWithDateAndXmId(Optional.empty(), testXmId, testXmKey, LocalDate.now());
    Assert.assertTrue(ApiResult.isOk(result));
    Map<?, ?> map = (Map<?, ?>) result.getData();
    System.out.println(map);
    Assert.assertFalse(map.isEmpty());

    //debug project
    result = controller.selectKpisWithDateAndXmId(Optional.empty(), 0, Project.PROJECT_MAP.get(0).getXmKey(),
        LocalDate.now());
    Assert.assertTrue(ApiResult.isOk(result));
    map = (Map<?, ?>) result.getData();
    System.out.println(map);
    Assert.assertFalse(map.isEmpty());
  }

  public void selectKpisWithDateRangeAndKpiId() {
    ApiResult<?> result = controller.selectKpisWithDateRangeAndKpiId(testXmId, testXmKey, testKpiId, LocalDate.now(),
        LocalDate.now().plusDays(1));
    Assert.assertTrue(ApiResult.isOk(result));
    Map<?, ?> map = (Map<?, ?>) result.getData();
    System.out.println(map);
    Assert.assertFalse(map.isEmpty());
  }

}
