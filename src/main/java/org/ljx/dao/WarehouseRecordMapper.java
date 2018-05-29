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

    final String INSERT_SQL = "insert into sys_warehouse_record (oddId,storeId,storeName,type,status,inOrOut,creatTime,confirmFlag,remark)values(#{oddId},#{storeId},#{storeName},#{type},#{status},#{inOrOut},#{creatTime},#{confirmFlag},#{remark})";
    final String UPDATE_SQL = "update sys_warehouse_record set storeId = #{storeId},storeName = #{storeName},type = #{type},status = #{status},inOrOut = #{inOrOut},creatTime = #{creatTime},remark = #{remark} where oddId = #{oddId}";
    final String SELECT_SQL = "select * from sys_warehouse_record where status = 1";
    final String FIND_SQL = "select * from sys_warehouse_record  where oddId = #{oddId}";
    final String DELETE_SQL = "delete from sys_warehouse_record where oddId = #{oddId}";

    @Insert(INSERT_SQL)
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
    List<WarehouseRecord> listType(@Param("storeId") byte storeId,@Param("inOrOut") byte inOrOut);

    static String buildList(@Param("storeId") byte storeId,@Param("inOrOut") byte inOrOut) {
        return new SQL(){{
            SELECT("*");
            FROM("sys_warehouse_record");
            if (storeId != 0) {
                WHERE("storeId = #{storeId}");
            }
            if (inOrOut != 0) {
                WHERE("inOrOut = #{inOrOut}");
            }
            WHERE("status = 1");
        }}.toString();
    }

    @Select(FIND_SQL)
    @ResultType(WarehouseRecord.class)
    WarehouseRecord findById(String oddId);
}
