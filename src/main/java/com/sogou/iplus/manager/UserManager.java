/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.manager;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import commons.saas.LoginService.User;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月20日;
//-------------------------------------------------------
@Service
public class UserManager {

  public static final Map<String, User> MAP = new HashMap<>();

  public User getUser(String value) {
    return MAP.get(value);
  }

}
