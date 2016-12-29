package com.sogou.iplus.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.sogou.iplus.entity.BusinessUnit;
import com.sogou.iplus.entity.Project;

import commons.spring.RedisRememberMeService.User;

public class Permission {

  public static final Map<String, Set<Integer>> MAP = new HashMap<>();

  public static boolean isAuthorized(String userId, List<Integer> kpiIds) {
    return MAP.getOrDefault(userId, new HashSet<>(kpiIds)).containsAll(kpiIds);
  }

  public static Set<Integer> getXmIds(String user) {
    Set<Integer> kpiIds = MAP.getOrDefault(user, new HashSet<>()), result = new HashSet<>();
    kpiIds.forEach(kpi -> result.add(Project.PROJECTS.stream()
        .filter(p -> p.getKpis().stream().filter(k -> Objects.equals(k.getKpiId(), kpi)).findFirst().isPresent())
        .findFirst().get().getXmId()));
    return result;
  }

  public static Set<Integer> get(User user) {
    return MAP.getOrDefault(Objects.isNull(user) ? "" : user.getId(), new HashSet<>());
  }

  public static void init() {
    MAP.put("xiaop_markwu", BusinessUnit.SUGARCAT.getProjects().stream().flatMap(project -> project.getKpis().stream())
        .map(kpi -> kpi.getKpiId()).collect(Collectors.toSet()));
  }
}
