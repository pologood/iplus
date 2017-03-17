package com.sogou.iplus.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import org.jsondoc.core.annotation.ApiObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.sogou.iplus.entity.BusinessUnit;
import com.sogou.iplus.entity.Company;
import com.sogou.iplus.entity.Kpi;
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
  public PermissionManager(Environment env) {
    WHITE_LIST = getSet(env, ",", "boss", "admin");
    init();
  }

  @Autowired
  XiaopLoginService pandoraLoginService;

  public static Set<String> WHITE_LIST;

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
    addKpiIds(projects.stream().flatMap(project -> project.getKpis().stream().map(kpi -> kpi.getKpiId()))
        .collect(Collectors.toList()), users);
  }

  private static void addKpiIds(List<Integer> kpiIds, String... users) {
    Arrays.stream(users).forEach(user -> MAP.computeIfAbsent(user, k -> new HashSet<>()).addAll(kpiIds));
  }

  public String getManagerList() {
    return String.join(",", getList());
  }

  public Collection<String> getList() {
    Set<String> set = MAP.keySet();
    try {
      permService.getPerms().stream().map(person -> person.getEmailName()).filter(name -> !WHITE_LIST.contains(name))
          .forEach(name -> set.add(name));;
    } catch (Exception e) {
      LOGGER.error("get perm list error", e);
    }
    return set;
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
    return WHITE_LIST.contains(userId) || getValidKpiIdsFromUser(user).containsAll(kpiIds)
        || MAP.getOrDefault(userId, new HashSet<>()).containsAll(kpiIds);
  }

  private Set<Integer> getValidKpiIdsFromUser(User user) {
    Set<Integer> result = new HashSet<>();
    try {
      Person person;
      if (Objects.isNull(user) || CollectionUtils.isEmpty(user.getPerms())
          || Objects.isNull(person = permService.getPerm(user.getUid())))
        return result;
      person.getPermsMap().keySet().stream().map(appId -> Project.PROJECT_MAP.get(getXmIdFromAppId(appId)))
          .filter(Objects::nonNull).forEach(project -> project.getKpis().forEach(kpi -> result.add(kpi.getKpiId())));
    } catch (Exception e) {
      LOGGER.error("get personal permission error", e);
    }
    return result;
  }

  private Integer getXmIdFromAppId(String appId) {
    return APPID_MAP.get(appId);
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

  public static final Map<String, Integer> APPID_MAP = new HashMap<>();

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
