package com.sogou.iplus.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Permission {

  public static final Map<String, Set<Integer>> MAP = new HashMap<>();

  static {
    MAP.put("wangwenlong", BusinessUnit.SUGARCAT.getProjects().stream().flatMap(project -> project.getKpis().stream())
        .map(kpi -> kpi.getKpiId()).collect(Collectors.toSet()));
  }

  public static boolean isAuthorized(String userId, List<Integer> kpiIds) {
    return MAP.getOrDefault("xiaop_" + userId, new HashSet<>(kpiIds)).containsAll(kpiIds);
  }

}
