package com.sogou.iplus.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Permission {

  public static final Map<String, List<Integer>> MAP = new HashMap<>();

  static {
    MAP.put("fengjin", BusinessUnit.SUGARCAT.getProjects().stream().flatMap(project -> project.getKpis().stream())
        .map(kpi -> kpi.getKpiId()).collect(Collectors.toList()));
  }

}
