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

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsondoc.core.annotation.ApiObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.sogou.iplus.entity.BusinessUnit;
import com.sogou.iplus.entity.Company;
import com.sogou.iplus.entity.Project;

import commons.saas.PermService;
import commons.saas.XiaopLoginService;
import commons.saas.PermService.Person;
import commons.spring.RedisRememberMeService;
import commons.spring.RedisRememberMeService.User;

@Service
public class PermissionManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(PermissionManager.class);

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
    init();
  }

  public static Set<String> BOSS_SET, ADMIN_SET;

  public static final Map<String, Set<Integer>> MAP = new HashMap<>();

  private static void init() {
    //test
    addProjects(Arrays.asList(Project.MOBILE_INPUT, Project.SUGAR_CAT), "tianyamin");

    addBus(BusinessUnit.SUGARCAT, "markwu", "toddlee", "wuxudong", "solomonlee", "liuzhankun");
    addBus(BusinessUnit.MARKETING, "ligang", "longchenxu", "zhangjinyi", "mapengfei");
    addProjects(Arrays.asList(Project.NEWS), "lizhi");
    addProjects(Arrays.asList(Project.CHINESE_MEDICINE), "buhailiang", "wangshanshan");
    addProjects(Arrays.asList(Project.PEDIA), "guoqi", "yuhongliang");
    addProjects(Arrays.asList(Project.PC_INPUT, Project.MOBILE_INPUT, Project.QQ_INPUT), "yanglei");
    addProjects(Arrays.asList(Project.QQ_INPUT, Project.MOBILE_INPUT), "lilin");
    addProjects(Arrays.asList(Project.PC_BROWSER, Project.MOBILE_BROWSER), "wujian");
    addProjects(Arrays.asList(Project.NAVIGATION), "kaiwang");
    addProjects(Arrays.asList(Project.APP_MARKET, Project.MOBILE_BROWSER), "wuzhiqiang");
    addProjects(Arrays.asList(Project.NAVIGATION, Project.APP_MARKET, Project.PC_BROWSER, Project.MOBILE_BROWSER),
        "casperwang");
    addProjects(Arrays.asList(Project.NAVIGATION, Project.APP_MARKET, Project.MOBILE_BROWSER), "yuanzhijun");
    addProjects(Arrays.asList(Project.VOICE), "wangyanfeng");
    addProjects(Arrays.asList(Project.NOVEL_SEARCH, Project.APP_SEARCH), "gaopeng");
    addProjects(Arrays.asList(Project.PICTURE_SEARCH, Project.SHOPPING_SEARCH), "huangxiaofeng");
    addProjects(Arrays.asList(Project.VEDIO_SEARCH), "jiangfeng", "wanggangbj7711");
    addProjects(Arrays.asList(Project.NOVEL_SEARCH, Project.APP_SEARCH, Project.PICTURE_SEARCH, Project.SHOPPING_SEARCH,
        Project.VEDIO_SEARCH), "tongzijian");
    addProjects(Arrays.asList(Project.SEARCH_APP), "wangxun", "yuhao", "wangxiaopeng");
    addProjects(Arrays.asList(Project.MAP), "zhouzhaoying", "kongxianglai");
    addProjects(Arrays.asList(Project.PC_SEARCH, Project.WIRELESS_SEARCH), "hanyifan", "yuhongxue");
    addProjects(Arrays.asList(Project.MOBILE_INPUT), "leiyu", "hulu", "zhuxiaofang");

    addProjects(
        Arrays.asList(Project.PC_INPUT, Project.MOBILE_INPUT, Project.QQ_INPUT, Project.PC_BROWSER,
            Project.MOBILE_BROWSER, Project.NAVIGATION, Project.APP_MARKET, Project.VOICE, Project.MAP),
        "xueyu", "liyangyang");
    addProjects(Arrays.asList(Project.PICTURE_SEARCH), "yuanshaofei");
    addProjects(Arrays.asList(Project.SHOPPING_SEARCH), "machao");
    addProjects(Arrays.asList(Project.APP_SEARCH), "yeming");
    addProjects(Arrays.asList(Project.NOVEL_SEARCH), "qinying", "zhangjinjing");
    addProjects(Arrays.asList(Project.WIRELESS_SEARCH, Project.PC_SEARCH, Project.SEARCH_APP, Project.MARKETING),
        "leiyu");
  }

  private static void addBus(BusinessUnit bu, String... users) {
    addProjects(bu.getProjects(), users);
  }

  private static void addProjects(List<Project> projects, String... users) {
    addKpiIds(projects.stream().flatMap(project -> project.getKpiList().stream()).collect(Collectors.toList()), users);
  }

  private static void addKpiIds(List<Integer> kpiIds, String... users) {
    Arrays.stream(users).forEach(user -> MAP.computeIfAbsent(user, k -> new HashSet<>()).addAll(kpiIds));
  }

  public List<Person> getPeople(Set<Role> roles, List<Person> people) {
    if (CollectionUtils.isEmpty(people) || CollectionUtils.isEmpty(roles)) return new ArrayList<>();
    return people.stream()
        .filter(p -> StringUtils.isNotBlank(p.getEmail()) && roles.contains(getRole(p.getEmailName())))
        .collect(Collectors.toList());
  }

  private Role getRole(String name) {
    if (BOSS_SET.contains(name)) return Role.BOSS;
    if (ADMIN_SET.contains(name)) return Role.ADMIN;
    return Role.MANAGER;
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
    if (Objects.isNull(user) || Objects.isNull(user.getOpenId()) || user.getOpenId().length() < 7) return false;
    String userName = user.getOpenId().substring(6);
    return isInWhiteList(userName) || getValidKpiIdsFromUser(user).containsAll(kpiIds)
        || MAP.getOrDefault(userName, new HashSet<>()).containsAll(kpiIds);
  }

  public boolean isInWhiteList(String name) {
    return BOSS_SET.contains(name) || ADMIN_SET.contains(name);
  }

  public Set<Integer> getValidKpiIdsFromUser(User user) {
    Person person;
    if (Objects.isNull(user) || !StringUtils.isNumeric(user.getId())
        || Objects.isNull(person = permService.getPerm(user.getUid())))
      return new HashSet<>();
    return getValidKpiIdsFromUser(person);
  }

  public Set<Integer> getValidKpiIdsFromUser(Person person) {
    try {
      return person.getPermsMap().keySet().stream().map(appId -> Project.getProjectWithAppId(appId))
          .filter(Objects::nonNull).flatMap(p -> p.getKpiList().stream()).collect(Collectors.toSet());
    } catch (Exception e) {
      LOGGER.error("get personal permission error", e);
      return new HashSet<>();
    }
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
