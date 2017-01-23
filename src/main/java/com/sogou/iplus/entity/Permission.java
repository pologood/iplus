package com.sogou.iplus.entity;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsondoc.core.annotation.ApiObject;

public class Permission {

  private Integer id;
  private String name;
  private String kpiIds;
  private Role role;

  public Set<Integer> getKpiIdSet() {
    return Arrays.stream(kpiIds.split(",")).map(Integer::parseInt).collect(Collectors.toSet());
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

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getKpiIds() {
    return kpiIds;
  }

  public void setKpiIds(String kpiIds) {
    this.kpiIds = kpiIds;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
