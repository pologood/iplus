package com.sogou.iplus.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Permission {

  private static final Logger LOGGER = LoggerFactory.getLogger(Permission.class);

  public static final Map<String, Set<Integer>> MAP = new HashMap<>();

  static {
    MAP.put("wangwenlong", BusinessUnit.SUGARCAT.getProjects().stream().flatMap(project -> project.getKpis().stream())
        .map(kpi -> kpi.getKpiId()).collect(Collectors.toSet()));
  }

  public static boolean isAuthorized(String userId, List<Integer> kpiIds) {
    LOGGER.info("user is {} kpiIds is {} map is {}", userId, kpiIds, MAP);
    return MAP.getOrDefault(userId, new HashSet<>(kpiIds)).containsAll(kpiIds);
  }

}
