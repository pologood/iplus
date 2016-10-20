/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.api;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import commons.saas.LoginService.User;
import commons.saas.XiaopLoginService;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月20日;
//-------------------------------------------------------
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

  XiaopLoginService service = new XiaopLoginService(null);

  public static final String COOKIE_NAME = "kpicookie";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String token = request.getParameter("token");
    if (StringUtils.isNotBlank(token)) {
      User user = service.login(token);
      if (Objects.nonNull(user)) {
        Cookie cookie = new Cookie(COOKIE_NAME, user.getName());
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(7));
        response.addCookie(cookie);
        request.setAttribute(CookieInterceptor.ATTRIBUTE_NAME, user);
      }
    }
    return true;
  }

}
