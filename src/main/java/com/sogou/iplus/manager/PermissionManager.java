package com.sogou.iplus.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsondoc.core.annotation.ApiObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.sogou.iplus.entity.Company;
import com.sogou.iplus.entity.Project;

import commons.saas.PermService;
import commons.saas.XiaopLoginService;
import commons.saas.PermService.Person;
import commons.spring.RedisRememberMeService;
import commons.spring.RedisRememberMeService.User;

@Service
public class PermissionManager {

  @Autowired
  RedisRememberMeService redisService;

  @Autowired
  PermService permService;

  @Autowired
  XiaopLoginService pandoraLoginService;

  @Autowired
  public PermissionManager(Environment env) {
    BOSS_SET = Arrays.stream(env.getRequiredProperty("boss").split(",")).collect(Collectors.toSet());
    ADMIN_SET = Arrays.stream(env.getRequiredProperty("admin").split(",")).collect(Collectors.toSet());
    BOSS_IDS = Arrays.stream(env.getRequiredProperty("bossids").split(",")).collect(Collectors.toSet());
    ADMIN_IDS = Arrays.stream(env.getRequiredProperty("adminids").split(",")).collect(Collectors.toSet());
  }

  public static Set<String> BOSS_SET, ADMIN_SET, BOSS_IDS, ADMIN_IDS;

  public List<String> getBossOrAdmin(Set<Role> roles) {
    Set<String> result = new HashSet<>();
    if (roles.remove(Role.BOSS)) result.addAll(BOSS_SET);
    if (roles.remove(Role.ADMIN)) result.addAll(ADMIN_SET);
    if (!roles.isEmpty()) throw new RuntimeException("unknown roles");
    return new ArrayList<>(result);
  }

  public List<Person> getManager() {
    return permService.getPerms().stream().filter(Objects::nonNull)
        .filter(person -> StringUtils.isNotBlank(person.getEmail()))
        .filter(person -> Role.MANAGER == getRole(person.getEmailName())).collect(Collectors.toList());
  }

  private Role getRole(String name) {
    if (BOSS_SET.contains(name)) return Role.BOSS;
    if (ADMIN_SET.contains(name)) return Role.ADMIN;
    return Role.MANAGER;
  }

  public boolean isAuthorized(User user, List<Integer> kpiIds) {
    return Objects.nonNull(user) && (isInWhiteList(user.getId()) || getValidKpiIdsFromUser(user).containsAll(kpiIds));
  }

  public boolean isInWhiteList(String user) {
    return BOSS_IDS.contains(user) || ADMIN_IDS.contains(user);
  }

  public Set<Integer> getValidKpiIdsFromUser(User user) {
    Set<Integer> result = new HashSet<>();
    if (Objects.nonNull(user) && CollectionUtils.isNotEmpty(user.getPerms()))
      user.getPerms().stream().filter(Objects::nonNull).filter(perm -> StringUtils.isNotBlank(perm.getEntity()))
          .map(perm -> Project.getProjectWithAppId(perm.getEntity())).filter(Objects::nonNull)
          .forEach(p -> p.getKpiList().forEach(id -> result.add(id)));
    return result;
  }

  public Set<Integer> getValidKpiIdsFromUser(Person person) {
    return person.getPermsMap().keySet().stream().map(appId -> Project.getProjectWithAppId(appId))
        .filter(Objects::nonNull).flatMap(p -> p.getKpiList().stream()).collect(Collectors.toSet());
  }

  public Company getCompany(Set<Integer> kpiIds) {
    if (CollectionUtils.isEmpty(kpiIds)) return Company.SOGOU;
    Company company = new Company(Company.SOGOU);
    company.remove(kpiIds);
    return company;
  }

  @ApiObject
  public enum Role {
    BOSS(1), ADMIN(2), MANAGER(3);
    private int value;

    private Role(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }
}
