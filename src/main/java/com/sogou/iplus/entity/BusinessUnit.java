/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.entity;

import org.jsondoc.core.annotation.ApiObject;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月11日;
//-------------------------------------------------------
@ApiObject(name = "BusinessUnit", description = "业务线")
public enum BusinessUnit {

  desktop("桌面"), search("搜索"), marketing("营销"), sugarcat("糖猫");

  String value;

  private BusinessUnit(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
