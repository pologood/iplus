package com.sogou.iplus.manager;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.sogou.iplus.entity.Kpi;
import com.sogou.iplus.entity.Project;
import com.sogou.iplus.manager.PermissionManager.Role;
import com.sogou.iplus.model.ApiResult;
import com.sogou.iplus.model.Errno;

import commons.saas.XiaopService;
import commons.saas.XiaopService.PushParam;

@Service
public class PushManager {

  private static final String LIMIT = "0.1";

  @Autowired
  PermissionManager permissionManager;

  @Autowired
  KpiManager kpiManager;

  @Autowired
  public PushManager(Environment env) {
    URL = env.getRequiredProperty("pandora.message.url");
    PERMISSION_URL = env.getRequiredProperty("pandora.message.permissionurl");
  }

  @Autowired
  XiaopService pandoraService;

  private static final int SIZE = 6;

  private String URL, PERMISSION_URL;

  public ApiResult<?> push(Set<Role> roles) {
    List<String> result = new ArrayList<>();
    List<Pair<Kpi, BigDecimal>> changes = kpiManager.getChange().stream()
        .filter(p -> p.getValue().abs().compareTo(new BigDecimal(LIMIT)) >= 0)
        .sorted(Collections.reverseOrder(Comparator.comparing(p -> p.getValue().abs()))).collect(Collectors.toList());
    if (roles.remove(Role.MANAGER)) permissionManager.getManager().stream()
        .map(person -> push(Arrays.asList(person.getEmailName()),
            changes.stream()
                .filter(change -> permissionManager.getValidKpiIdsFromUser(person).contains(change.getKey().getKpiId()))
                .collect(Collectors.toList()),
            getUrlWithDate(PERMISSION_URL)))
        .filter(StringUtils::isNotBlank).forEach(result::add);
    String message;
    if (!roles.isEmpty() && StringUtils
        .isNotBlank(message = push(permissionManager.getBossOrAdmin(roles), changes, getUrlWithDate(URL))))
      result.add(message);
    return result.isEmpty() ? ApiResult.ok() : new ApiResult<>(Errno.INTERNAL_ERROR, result);
  }

  public List<List<Pair<Kpi, BigDecimal>>> split(List<Pair<Kpi, BigDecimal>> changes) {
    List<List<Pair<Kpi, BigDecimal>>> result = Arrays.asList(new ArrayList<>(), new ArrayList<>());
    changes.stream().limit(SIZE).forEach(pair -> {
      if (BigDecimal.ZERO.compareTo(pair.getValue()) > 0) result.get(1).add(pair);
      else result.get(0).add(pair);
    });
    return result;
  }

  private String push(List<String> people, List<Pair<Kpi, BigDecimal>> changes, String url) {
    List<List<Pair<Kpi, BigDecimal>>> waves = split(changes);
    return push(String.format("[%s]数据已更新", LocalDate.now()), getMessage(waves), String.join(",", people), url);
  }

  private String getTitle(List<List<Pair<Kpi, BigDecimal>>> waves) {
    return waves.get(0).isEmpty() && waves.get(1).isEmpty() ? NO_WAVES_TITLE_MESSAGE
        : isHoliday() ? HOLIDAY_TITLE_MESSAGE : WAVES_TITLE_MESSAGE;
  }

  private String getMessage(List<List<Pair<Kpi, BigDecimal>>> waves) {
    return String.join("\n", getTitle(waves), String.join("\n",
        waves.stream().flatMap(list -> list.stream()).map(p -> getWave(p)).collect(Collectors.toList())));
  }

  private String getWave(Pair<Kpi, BigDecimal> pair) {
    Kpi kpi = Project.getKpi(pair.getKey().getKpiId());
    String kpiName = Objects.nonNull(kpi.getShortName1()) ? kpi.getShortName1() : kpi.getKpiName();
    return String.format("%s %s %d%%", kpiName, pair.getValue().compareTo(BigDecimal.ZERO) > 0 ? UP_TEXT : DOWN_TEXT,
        pair.getValue().multiply(new BigDecimal(100)).abs().intValue());
  }

  private boolean isHoliday() {
    LocalDate today = LocalDate.now().minusDays(1), yesterday = today.minusDays(1);
    return getType(today.getDayOfWeek()) != getType(yesterday.getDayOfWeek());
  }

  private int getType(DayOfWeek dayOfWeek) {
    return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY ? 0 : 1;
  }

  private String getUrlWithDate(String url) {
    return url + "?baseDate=" + LocalDate.now().toString();
  }

  public String push(String title, String message, String list, String url) {
    PushParam param = new PushParam();
    param.setTitle(title);
    param.setMessage(message);
    param.setOpenId(list);
    param.setUrl(url);
    return pandoraService.push(param);
  }

  static final String UP_TEXT = "↑", DOWN_TEXT = "↓", NO_WAVES_TITLE_MESSAGE = "今日无明显波动",
      WAVES_TITLE_MESSAGE = "今日数据波动幅度较大的产品", HOLIDAY_TITLE_MESSAGE = "今日受节假日影响，数据产生周期性波动";

}
