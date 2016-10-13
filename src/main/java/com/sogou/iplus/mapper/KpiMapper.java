/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import com.sogou.iplus.entity.Kpi;

//--------------------- Change Logs----------------------
//@author wangwenlong Initial Created at 2016年10月12日;
//-------------------------------------------------------
@Repository
public interface KpiMapper {

  class Sql {

    public static final String TABLE = "`kpi`";

    public static String select(Kpi kpi) {
      return new SQL().SELECT("xmId,kpiId,createDate,kpi").FROM(TABLE).WHERE("xmId = #{xmId}").WHERE("kpiId = #{kpiId}")
          .WHERE("createDate = #{createDate}").toString();
    }

    public static String add(Kpi kpi) {
      return new SQL().INSERT_INTO(TABLE).VALUES("xmId", "#{xmId}").VALUES("kpiId", "#{kpiId}")
          .VALUES("createDate", "#{createDate}").VALUES("kpi", "#{kpi}").toString();
    }

    public static String update(Kpi kpi) {
      return new SQL().UPDATE(TABLE).SET("kpi = #{kpi}").WHERE("xmId = #{xmId}").WHERE("kpiId = #{kpiId}")
          .WHERE("createDate = #{createDate}").toString();
    }

  }

  @InsertProvider(type = Sql.class, method = "add")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int add(Kpi kpi);

  @SelectProvider(type = Sql.class, method = "select")
  Kpi select(Kpi kpi);

  @UpdateProvider(type = Sql.class, method = "update")
  int update(Kpi kpi);

}
