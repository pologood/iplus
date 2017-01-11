package com.sogou.iplus.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.sogou.iplus.entity.BusinessUnit;
import com.sogou.iplus.entity.Company;
import com.sogou.iplus.entity.Kpi;
import com.sogou.iplus.entity.Project;

import commons.saas.XiaopLoginService;
import commons.spring.RedisRememberMeService;
import commons.spring.RedisRememberMeService.User;

@Service
public class PermissionManager {

  @Autowired
  RedisRememberMeService redisService;

  @Autowired
  public PermissionManager(Environment env) {
    WHITE_LIST = getSet(env, ",", "boss", "admin");
  }

  @Autowired
  XiaopLoginService pandoraLoginService;

  public Set<String> WHITE_LIST;

  public static final Map<String, Set<Integer>> MAP = new HashMap<>();

  public static void init() {
    Stream.of("markwu", "toddlee", "wuxudong", "solomonlee", "liuzhankun")
        .forEach(s -> MAP.put(s, BusinessUnit.SUGARCAT.getProjects().stream()
            .flatMap(project -> project.getKpis().stream().map(kpi -> kpi.getKpiId())).collect(Collectors.toSet())));
  }

  public static String getManagerList() {
    return String.join(",", MAP.keySet());
  }

  public User login(Optional<String> token, HttpServletResponse response) {
    if (!token.isPresent()) return null;
    commons.saas.LoginService.User pandoraUser = pandoraLoginService.login(token.get());
    if (Objects.isNull(pandoraUser)) return null;
    User user = new User(pandoraUser.getOpenId(), pandoraUser.getName());
    redisService.login(response, user);
    return user;
  }

  public boolean isAuthorized(User user, List<Integer> kpiIds) {
    if (Objects.isNull(user)) return false;
    String userId = user.getId().substring(6);
    return WHITE_LIST.contains(userId) || MAP.getOrDefault(userId, new HashSet<>()).containsAll(kpiIds);
  }

  public Company getCompany(User user) {
    Set<Integer> set;
    if (Objects.isNull(user) || CollectionUtils.isEmpty(set = MAP.get(user.getId().substring(6)))) return Company.SOGOU;
    Company company = new Company(), sogou = Company.SOGOU;

    company.setId(sogou.getId());
    company.setName(sogou.getName());
    company.setKpis(sogou.getKpis());
    company.setBusinessUnits(new ArrayList<>());

    for (BusinessUnit sogouBu : sogou.getBusinessUnits()) {
      BusinessUnit bu = new BusinessUnit();

      bu.setId(sogouBu.getId());
      bu.setName(sogouBu.getName());
      bu.setKpis(sogouBu.getKpis());
      bu.setProjects(new ArrayList<>());

      for (Project sogouProject : sogouBu.getProjects()) {
        Project project = new Project(sogouProject);

        for (Kpi kpi : sogouProject.getKpis())
          if (!set.contains(kpi.getKpiId()))
            project.getKpis().removeIf(k -> Objects.equals(k.getKpiId(), kpi.getKpiId()));

        if (!project.getKpis().isEmpty()) bu.getProjects().add(project);
      }

      if (!bu.getProjects().isEmpty()) company.getBusinessUnits().add(bu);
    }

    return company;
  }

  public Set<String> getSet(Environment env, String regex, String... keys) {
    return Arrays.stream(keys).flatMap(key -> Arrays.stream(env.getRequiredProperty(key).split(regex)))
        .collect(Collectors.toSet());
  }
}
