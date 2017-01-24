package com.sogou.iplus.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.sogou.iplus.entity.BusinessUnit;
import com.sogou.iplus.entity.Company;
import com.sogou.iplus.entity.Kpi;
import com.sogou.iplus.entity.Permission;
import com.sogou.iplus.entity.Project;
import com.sogou.iplus.entity.Permission.Role;
import com.sogou.iplus.mapper.PermissionMapper;
import com.sogou.iplus.model.ApiResult;

import commons.saas.XiaopLoginService;
import commons.spring.RedisRememberMeService;
import commons.spring.RedisRememberMeService.User;

@Service
public class PermissionManager implements InitializingBean {

  @Autowired
  RedisRememberMeService redisService;

  @Autowired
  PermissionMapper permissionMapper;

  @Autowired
  XiaopLoginService pandoraLoginService;

  public static final Set<String> WHITE_LIST = new HashSet<>();

  public static final Map<String, Set<Integer>> MAP = new HashMap<>();

  public synchronized void init() {
    clear();
    List<Permission> permissions = permissionMapper.selectAll();
    for (Permission p : permissions)
      if (p.getRole() == Role.MANAGER) MAP.put(p.getName(), p.getKpiIdSet());
      else WHITE_LIST.add(p.getName());
  }

  private void clear() {
    WHITE_LIST.clear();
    MAP.clear();
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

  @Override
  public void afterPropertiesSet() throws Exception {
    init();
  }

  public ApiResult<?> add(List<String> names, List<String> projects, Role role) {
    names.forEach(name -> {
      try {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setRole(role);
        if (CollectionUtils.isNotEmpty(projects)) permission.setKpiIds(String.join(",",
            projects.stream()
                .flatMap(p -> getProject(p).getKpis().stream().map(kpi -> kpi.getKpiId().toString()).sorted())
                .collect(Collectors.toList())));
        permissionMapper.add(permission);
      } catch (DuplicateKeyException e) {
      }
    });
    init();
    return ApiResult.ok();
  }

  private Project getProject(String name) {
    return Project.PROJECTS.stream().filter(p -> name.equalsIgnoreCase(p.getProjectName())).findFirst().orElse(null);
  }
}
