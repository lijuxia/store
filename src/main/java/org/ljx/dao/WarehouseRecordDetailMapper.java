package org.ljx.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.ljx.entity.WarehouseRecordDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ljx on 2018/5/17.
 */
@Mapper
@Repository
public interface WarehouseRecordDetailMapper {

    final String INSERT_SQL = "insert into sys_warehouse_record_detail (name,username,password,type,status)values(#{name},#{username},#{password},#{type},#{status})";
    final String UPDATE_SQL = "update sys_warehouse_record_detail set name = #{name},username = #{username},password = #{password},type = #{type},status = #{status} where id = #{id}";
    final String SELECT_SQL = "select * from sys_warehouse_record_detail where status = 1";
    final String FIND_SQL = "select * from sys_warehouse_record_detail  where id = #{id}";
    final String DELETE_SQL = "delete from sys_warehouse_record_detail where id = #{id}";

    @Insert(INSERT_SQL)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(WarehouseRecordDetail warehouseRecordDetail);

    @Delete(DELETE_SQL)
    void delete(int id);

    @Update(UPDATE_SQL)
    void update(WarehouseRecordDetail warehouseRecordDetail);

    @Select(SELECT_SQL)
    @ResultType(WarehouseRecordDetail.class)
    List<WarehouseRecordDetail> list();

    @SelectProvider(type = WarehouseRecordDetailMapper.class, method = "buildList")
    @ResultType(WarehouseRecordDetail.class)
    List<WarehouseRecordDetail> listType(@Param("type") byte type);

    static String buildList(@Param("type") byte type) {
        return new SQL(){{
            SELECT("*");
            FROM("sys_warehouse_record_detail");
            if (type != 0) {
                WHERE("type = #{type}");
            }
            WHERE("status = 1");
        }}.toString();
    }

    @Select(FIND_SQL)
    @ResultType(WarehouseRecordDetail.class)
    WarehouseRecordDetail findById(int id);
}
