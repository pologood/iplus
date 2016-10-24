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

    public static String add(Kpi kpi) {
      if (Objects.isNull(kpi.getCreateTime())) kpi.setCreateTime(LocalDateTime.now());
      return new SQL().INSERT_INTO(TABLE).VALUES("xmId", "#{xmId}").VALUES("kpiId", "#{kpiId}")
          .VALUES("kpiDate", "#{kpiDate}").VALUES("kpi", "#{kpi}").VALUES("createTime", "#{createTime}").toString();
    }

    public static String update(Kpi kpi) {
      return new SQL().UPDATE(TABLE).SET("kpi = #{kpi}").WHERE("xmId = #{xmId}").WHERE("kpiId = #{kpiId}")
          .WHERE("kpiDate = #{kpiDate}").toString();
    }

    public static String select(Map<String, Object> map) {
      SQL sql = new SQL().SELECT(ITEMS).FROM(TABLE);
      if (0 != MapUtils.getIntValue(map, "xmId")) sql.WHERE("xmId = #{xmId}");
      if (0 != MapUtils.getIntValue(map, "kpiId")) sql.WHERE("kpiId = #{kpiId}");
      LocalDate date = (LocalDate) map.get("date");
      if (Objects.nonNull(date))
        sql.WHERE("createTime >= #{date}").WHERE(String.format("createTime < '%s'", date.plusDays(1)));
      if (MapUtils.getBooleanValue(map, "isValid")) sql.WHERE(String.format("kpi != %s", Integer.MIN_VALUE));
      return sql.toString();
    }

    public static String selectWithDateRangeAndKpiId(Map<String, Object> map) {
      SQL sql = new SQL().SELECT(ITEMS).FROM(TABLE).WHERE("kpiId = #{kpiId}").WHERE("createTime >= #{beginDate}")
          .WHERE("createTime <= #{endDate}");
      if (Objects.nonNull(map.get("xmId"))) sql.WHERE("xmId = #{xmId}");
      if (MapUtils.getBooleanValue(map, "isValid")) sql.WHERE(String.format("kpi != %s", Integer.MIN_VALUE));
      return sql.toString();
    }

    public static String selectWithDateAndXmId(Map<String, Object> map) {
      SQL sql = new SQL().SELECT(ITEMS).FROM(TABLE).WHERE("createTime >= #{date}")
          .WHERE(String.format("createTime < '%s'", ((LocalDate) map.get("date")).plusDays(1)));
      if (0 != MapUtils.getIntValue(map, "xmId")) sql.WHERE("xmId = #{xmId}");
      if (MapUtils.getBooleanValue(map, "isValid")) sql.WHERE(String.format("kpi != %s", Integer.MIN_VALUE));
      return sql.toString();
    }

  }

  @InsertProvider(type = Sql.class, method = "add")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int add(Kpi kpi);

  @UpdateProvider(type = Sql.class, method = "update")
  int update(Kpi kpi);

  @SelectProvider(type = Sql.class, method = "select")
  List<Kpi> select(@Param("xmId") Integer xmId, @Param("kpiId") Integer kpiId, @Param("date") LocalDate date,
      @Param("isValid") boolean isValid);

  @SelectProvider(type = Sql.class, method = "selectWithDateRangeAndKpiId")
  List<Kpi> selectWithDateRangeAndKpiId(@Param("xmId") int xmId, @Param("kpiId") int kpiId,
      @Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate, @Param("isValid") boolean isValid);

  @SelectProvider(type = Sql.class, method = "selectWithDateAndXmId")
  List<Kpi> selectWithDateAndXmId(@Param("xmId") int xmId, @Param("date") LocalDate date,
      @Param("isValid") boolean isValid);

}
