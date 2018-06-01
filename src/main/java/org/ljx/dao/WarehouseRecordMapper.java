package org.ljx.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.ljx.entity.WarehouseRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ljx on 2018/5/17.
 */
@Mapper
@Repository
public interface WarehouseRecordMapper {

    final String INSERT_SQL = "insert into sys_warehouse_record (oddId,storeId,storeName,type,status,inOrOut,creatTime,confirmFlag,remark)values(#{oddId},#{username},#{password},#{type},#{status})";
    final String UPDATE_SQL = "update sys_warehouse_record set name = #{name},username = #{username},password = #{password},type = #{type},status = #{status} where id = #{id}";
    final String SELECT_SQL = "select * from sys_warehouse_record where status = 1";
    final String FIND_SQL = "select * from sys_warehouse_record  where id = #{id}";
    final String DELETE_SQL = "delete from sys_warehouse_record where id = #{id}";

    @Insert(INSERT_SQL)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(WarehouseRecord warehouseRecord);

    @Delete(DELETE_SQL)
    void delete(int id);

    @Update(UPDATE_SQL)
    void update(WarehouseRecord warehouseRecord);

    @Select(SELECT_SQL)
    @ResultType(WarehouseRecord.class)
    List<WarehouseRecord> list();

    @SelectProvider(type = WarehouseRecordMapper.class, method = "buildList")
    @ResultType(WarehouseRecord.class)
    List<WarehouseRecord> listType(@Param("type") byte type);

    static String buildList(@Param("type") byte type) {
        return new SQL(){{
            SELECT("*");
            FROM("sys_warehouse_record");
            if (type != 0) {
                WHERE("type = #{type}");
            }
            WHERE("status = 1");
        }}.toString();
    }

    @Select(FIND_SQL)
    @ResultType(WarehouseRecord.class)
    WarehouseRecord findById(int id);
}
