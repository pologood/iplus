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
import java.util.stream.Collectors;

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

import com.sogou.iplus.api.KpiController;
import com.sogou.iplus.config.DaoConfig;
import com.sogou.iplus.config.RootConfig;
import com.sogou.iplus.entity.Kpi;
import com.sogou.iplus.entity.Project;
import com.sogou.iplus.mapper.KpiMapper;
import com.sogou.iplus.model.ApiResult;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月14日;
//-------------------------------------------------------
@ContextConfiguration(classes = { RootConfig.class, DaoConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class KpiControllerTest {

  @Mock
  private HttpServletRequest request;

  private LocalDate date = LocalDate.of(2015, 11, 11);

  @Autowired
  private KpiController controller;

  @Autowired
  private KpiMapper mapper;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void add() {
    Project project = Project.PROJECT_MAP.get(70);
    for (Kpi kpi : project.getKpis())
      Mockito.when(request.getParameter(kpi.getKpiId().toString())).thenReturn(kpi.getKpiId().toString());
    controller.add(request, project.getProjectId(), project.getProjectKey(), Optional.of(date));
    Map<Integer, Kpi> kpiMap = mapper.selectKpisWithDate(date).stream()
        .collect(Collectors.toMap(k -> k.getKpiId(), k -> k));
    Assert.assertEquals(project.getKpis().size(), kpiMap.size());
    project.getKpis()
        .forEach(k -> Assert.assertEquals(k.getKpiId().intValue(), kpiMap.get(k.getKpiId()).getKpi().intValue()));
  }

  @Test
  public void selectNull() {
    ApiResult<?> result = controller.selectProjectsDoNotSubmitKpiOnNamedDate(Optional.of(date));
    Assert.assertEquals(result.getCode(), ApiResult.ok().getCode());
    List<?> objects = (List<?>) result.getData();
    Assert.assertFalse(objects.isEmpty());
    Assert.assertEquals(0,
        objects.stream().map(o -> (Project) o).filter(p -> Objects.equals(70, p.getProjectId())).count());
  }

}
