package com.sogou.iplus.manager;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.jsondoc.core.annotation.ApiObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;
import com.sogou.iplus.model.ApiResult;

import commons.saas.XiaopService;
import commons.saas.XiaopService.PushParam;

@Service
public class PushManager {

  @Autowired
  private Environment env;

  private Map<Role, String> LIST_MAP = ImmutableMap.of(Role.BOSS, env.getRequiredProperty("boss"), Role.ADMIN,
      env.getRequiredProperty("admin"));

  @Autowired
  XiaopService pandoraService;

  @ApiObject
  public enum Role {
    BOSS, ADMIN, MANAGER;
  }

  private String MESSAGE = "今日搜狗业务指标已更新，请点击查看", TITLE = "数据已更新",
      COVER = env.getRequiredProperty("pandora.message.cover"), URL = env.getRequiredProperty("pandora.message.url"),
      PERMISSION_URL = env.getRequiredProperty("pandora.message.permissionurl");

  public ApiResult<?> push(List<Role> roles) {
    String result1 = null, result2 = null, result;

    if (roles.remove(Role.MANAGER)) result1 = push(PermissionManager.getManagerList(), PERMISSION_URL);
    result2 = push(getList(roles), URL);
    result = String.join("\n", Stream.of(result1, result2).filter(Objects::nonNull).collect(Collectors.toList()));

    return StringUtils.isBlank(result) ? ApiResult.ok() : ApiResult.internalError(result);
  }

  public String push(String list, String url) {
    if (StringUtils.isAnyBlank(list, url)) return null;
    PushParam param = new PushParam();

    param.setImage(COVER);
    param.setMessage(MESSAGE);
    param.setOpenId(list);
    param.setTitle(String.format("[%s]%s", LocalDate.now(), TITLE));
    param.setUrl(url);

    return pandoraService.push(param);
  }

  private String getList(List<Role> roles) {
    return String.join(",", roles.stream().map(role -> LIST_MAP.get(role)).collect(Collectors.toList()));
  }
}
