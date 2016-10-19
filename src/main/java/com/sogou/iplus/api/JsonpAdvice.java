/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.api;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月18日;
//-------------------------------------------------------

@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {

  public JsonpAdvice() {
    super("jsonp");
  }
}
