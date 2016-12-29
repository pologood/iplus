package com.sogou.iplus.manager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.sogou.iplus.entity.BusinessUnit;

import commons.spring.RedisRememberMeService.User;

public class Permission {

  public static final Map<String, Set<Integer>> MAP = new HashMap<>();

  public static boolean isAuthorized(String userId, List<Integer> kpiIds) {
    if (MAP.isEmpty()) init();
    return MAP.getOrDefault(userId, new HashSet<>(kpiIds)).containsAll(kpiIds);
  }

  public static Set<Integer> get(User user) {
    if (MAP.isEmpty()) init();
    return Permission.MAP.getOrDefault(Objects.isNull(user) ? "" : user.getId(), new HashSet<>());
  }

  public static void init() {
    MAP.put("xiaop_fengjin", BusinessUnit.SUGARCAT.getProjects().stream().flatMap(project -> project.getKpis().stream())
        .map(kpi -> kpi.getKpiId()).collect(Collectors.toSet()));
    MAP.put("xiaop_markwu", BusinessUnit.SUGARCAT.getProjects().stream().flatMap(project -> project.getKpis().stream())
        .map(kpi -> kpi.getKpiId()).collect(Collectors.toSet()));
    MAP.put("xiaop_wangjialin",
        Arrays.asList(BusinessUnit.SUGARCAT, BusinessUnit.MARKETING).stream().flatMap(bu -> bu.getProjects().stream())
            .flatMap(project -> project.getKpis().stream()).map(kpi -> kpi.getKpiId()).collect(Collectors.toSet()));
    MAP.put("xiaop_wangwenlong", BusinessUnit.SUGARCAT.getProjects().stream()
        .flatMap(project -> project.getKpis().stream()).map(kpi -> kpi.getKpiId()).collect(Collectors.toSet()));
  }
}
