/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package commons.saas;

import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import commons.utils.DigestHelper;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月27日;
//-------------------------------------------------------
public class XiaopService {

  private static final String URL = "https://puboa.sogou-inc.com/moa/sylla/mapi/pns/send";

  private String appId;

  private String appKey;

  private RestTemplate restTemplate;

  public XiaopService() {
    this(new RestTemplate());
  }

  public XiaopService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public XiaopService setAppId(String appId) {
    this.appId = appId;
    return this;
  }

  public XiaopService setAppKey(String appKey) {
    this.appKey = appKey;
    return this;
  }

  public static class PushParam {

    private String openId;

    private String title;

    private String message;

    private String image;

    private String url;

    private boolean isHTML;

    public PushParam setOpenId(String openId) {
      this.openId = openId;
      return this;
    }

    public String getOpenId() {
      return this.openId;
    }

    public PushParam setTitle(String title) {
      this.title = title;
      return this;
    }

    public String getTitle() {
      return this.title;
    }

    public PushParam setMessage(String message) {
      this.message = message;
      return this;
    }

    public String getMessage() {
      return this.message;
    }

    public PushParam setImage(String image) {
      this.image = image;
      return this;
    }

    public String getImage() {
      return this.image;
    }

    public PushParam setUrl(String url) {
      this.url = url;
      return this;
    }

    public String getUrl() {
      return this.url;
    }

    public boolean isHTML() {
      return isHTML;
    }

    public void setHTML(boolean isHTML) {
      this.isHTML = isHTML;
    }
  }

  public String push(PushParam param) {
    String now = String.valueOf(System.currentTimeMillis());
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("pubid", appId);
    map.add("token", getToken(now));
    map.add("ts", now);
    map.add("to", param.getOpenId());
    map.add("title", param.getTitle());
    map.add("summary", param.getMessage());
    map.add("content", param.getMessage());
    if (param.isHTML()) map.add("tp", "8");
    else if (param.getImage() != null) {
      map.add("cover", param.getImage());
      map.add("tp", "1");
    } else {
      map.add("tp", "3");
    }
    map.add("url", param.getUrl());

    try {
      @SuppressWarnings("unchecked")
      Map<String, Object> ret = restTemplate.postForObject(URL, map, Map.class);
      int code = (Integer) ret.get("status");
      if (code != 0) return (String) ret.get("statusText");
      else return null;
    } catch (Exception e) {
      return e.toString();
    }
  }

  private String getToken(String now) {
    return DigestHelper.md5(String.join(":", appId, appKey, now).getBytes());
  }
}
