package com.sogou.iplus.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProjectInfo {
  public static final String PKG_PREFIX    = "com.sogou.iplus";
  public static final String API_PKG       = "com.sogou.iplus.api";
  public static final String MAPPER_PKG    = "com.sogou.iplus.mapper";
  public static final List<String> DOC_PKG = Collections.unmodifiableList(
    Arrays.asList("com.sogou.iplus.api", "com.sogou.iplus.entity", "com.sogou.iplus.model"));
}
