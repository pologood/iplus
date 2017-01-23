package com.sogou.iplus.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.sogou.iplus.entity.Permission;

@Repository
@Lazy
public interface PermissionMapper {

  class Sql {
    private static final String TABLE = "authorization", SELECT_ALL = "select * from " + TABLE;

    public static final String add(Permission permission) {
      return new SQL().INSERT_INTO(TABLE).VALUES("name", "#{name}").VALUES("kpiIds", "#{kpiIds}")
          .VALUES("role", "#{role}").toString();
    }
  }

  @InsertProvider(type = Sql.class, method = "add")
  int add(Permission permission);

  @Select(Sql.SELECT_ALL)
  List<Permission> selectAll();

}
