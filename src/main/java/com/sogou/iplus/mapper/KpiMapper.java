/*
 * $Id$
 *
 * Copyright (c) 2015 Sogou.com. All Rights Reserved.
 */
package com.sogou.iplus.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.MapUtils;
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

    public static final String ITEMS = "`xmId`, `kpiId`, `kpiDate`, `kpi`, `createTime`, `updateTime`";

    public static String select(Kpi kpi) {
      return new SQL().SELECT(ITEMS).FROM(TABLE).WHERE("xmId = #{xmId}").WHERE("kpiId = #{kpiId}")
          .WHERE("kpiDate = #{kpiDate}").toString();
    }

    public static String add(Kpi kpi) {
      kpi.setCreateTime(LocalDateTime.now());
      return new SQL().INSERT_INTO(TABLE).VALUES("xmId", "#{xmId}").VALUES("kpiId", "#{kpiId}")
          .VALUES("kpiDate", "#{kpiDate}").VALUES("kpi", "#{kpi}").VALUES("createTime", "#{createTime}").toString();
    }

    public static String update(Kpi kpi) {
      return new SQL().UPDATE(TABLE).SET("kpi = #{kpi}").WHERE("xmId = #{xmId}").WHERE("kpiId = #{kpiId}")
          .WHERE("kpiDate = #{kpiDate}").toString();
    }

    public static String selectKpisWithKpiDate(LocalDate kpiDate) {
      return new SQL().SELECT(ITEMS).FROM(TABLE).WHERE("kpiDate = #{kpiDate}").toString();
    }

    public static String selectKpisWithCreateTime(LocalDate date) {
      SQL sql = new SQL().SELECT(ITEMS).FROM(TABLE).WHERE("createTime >= #{date}");
      LocalDate tomorrow = date.plusDays(1);
      sql.WHERE(String.format("createTime < '%s'", tomorrow));
      return sql.toString();
    }

    public static String selectKpisWithKpiDateRangeAndProjectId(Map<String, Object> map) {
      SQL sql = new SQL().SELECT(ITEMS).FROM(TABLE).WHERE("kpiDate >= #{beginDate}").WHERE("kpiDate <= #{endDate}");
      if (Objects.nonNull(map.get("projectId"))) sql.WHERE("xmId = #{projectId}");
      return sql.toString();
    }

    public static String selectKpisWithKpiDateAndProjectId(Map<String, Object> map) {
      SQL sql = new SQL().SELECT(ITEMS).FROM(TABLE).WHERE("kpiDate = #{date}");
      if (0 != MapUtils.getIntValue(map, "projectId")) sql.WHERE("xmId = #{projectId}");
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

  @SelectProvider(type = Sql.class, method = "selectKpisWithKpiDate")
  List<Kpi> selectKpisWithKpiDate(LocalDate date);

  @SelectProvider(type = Sql.class, method = "selectKpisWithCreateTime")
  List<Kpi> selectKpisWithCreateDate(LocalDate date);

  @SelectProvider(type = Sql.class, method = "selectKpisWithKpiDateRangeAndProjectId")
  List<Kpi> selectKpisWithKpiDateRangeAndProjectId(@Param("projectId") Integer projectId,
      @Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);

  @SelectProvider(type = Sql.class, method = "selectKpisWithKpiDateAndProjectId")
  List<Kpi> selectKpisWithKpiDateAndProjectId(@Param("projectId") int projectId, @Param("date") LocalDate date);

}
