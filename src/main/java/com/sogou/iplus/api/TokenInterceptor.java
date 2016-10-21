/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.api;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import commons.saas.LoginService.User;
import commons.saas.XiaopLoginService;
import commons.spring.RedisRememberMeService;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月20日;
//-------------------------------------------------------
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  RedisRememberMeService redisService;

  @Autowired
  XiaopLoginService pandoraService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String token = request.getParameter("token");
    if (StringUtils.isNotBlank(token)) {
      User user = pandoraService.login(token);
      if (Objects.nonNull(user))
        redisService.login(response, new commons.spring.RedisRememberMeService.User(user.getOpenId(), user.getName()));
    }
    return true;
  }

}
