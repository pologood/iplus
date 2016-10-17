/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
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

    public static final String ITEMS = "`xmId`, `kpiId`, `createDate`, `kpi`";

    public static String select(Kpi kpi) {
      return new SQL().SELECT(ITEMS).FROM(TABLE).WHERE("xmId = #{xmId}").WHERE("kpiId = #{kpiId}")
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

    public static String selectKpisWithDate(LocalDate createDate) {
      return new SQL().SELECT(ITEMS).FROM(TABLE).WHERE("createDate = #{createDate}").toString();
    }

    public static String selectKpisWithDateAndProjectId(Map<String, Object> map) {
      SQL sql = new SQL().SELECT(ITEMS).FROM(TABLE).WHERE("createDate >= #{beginDate}")
          .WHERE("createDate <= #{endDate}");
      if (map.containsKey("projectId")) sql.WHERE("xmId = #{projectId}");
      return sql.toString();
    }
  }

  @InsertProvider(type = Sql.class, method = "add")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int add(Kpi kpi);

  @SelectProvider(type = Sql.class, method = "select")
  Kpi select(Kpi kpi);

  @UpdateProvider(type = Sql.class, method = "update")
  int update(Kpi kpi);

  @SelectProvider(type = Sql.class, method = "selectKpisWithDate")
  List<Kpi> selectKpisWithDate(LocalDate date);

  @SelectProvider(type = Sql.class, method = "selectKpisWithDateAndProjectId")
  List<Kpi> selectKpisWithDateAndProjectId(@Param("projectId") Integer projectId,
      @Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);

}
