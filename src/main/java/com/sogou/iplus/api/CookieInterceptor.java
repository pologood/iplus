/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.api;

import java.util.Arrays;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sogou.iplus.manager.UserManager;

import commons.saas.LoginService.User;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月20日;
//-------------------------------------------------------
@Component
public class CookieInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  private UserManager userManager;

  public static final String ATTRIBUTE_NAME = "kpiuser";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    Cookie[] cookies = request.getCookies();
    if (Objects.nonNull(cookies)) {
      Cookie userCookie = Arrays.stream(cookies).filter(Objects::nonNull)
          .filter(cookie -> Objects.equals(cookie.getName(), TokenInterceptor.COOKIE_NAME)).findFirst().orElse(null);
      if (Objects.nonNull(userCookie)) {
        User user = userManager.getUser(userCookie.getValue());
        if (Objects.nonNull(user)) request.setAttribute(ATTRIBUTE_NAME, user);
      }
    }
    return true;
  }

}
