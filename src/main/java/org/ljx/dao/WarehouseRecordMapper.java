package org.ljx.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.ljx.entity.WarehouseRecord;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ljx on 2018/5/17.
 */
@Mapper
@Repository
public interface WarehouseRecordMapper {

    final String INSERT_SQL = "insert into sys_warehouse_record (oddId,storeId,storeName,type,status,inOrOut,creatTime,confirmFlag,remark,sendStoreId,sendStoreName)values(#{oddId},#{storeId},#{storeName},#{type},#{status},#{inOrOut},#{creatTime},#{confirmFlag},#{remark},#{sendStoreId},#{sendStoreName})";
    final String UPDATE_SQL = "update sys_warehouse_record set storeId = #{storeId},storeName = #{storeName},type = #{type},status = #{status},inOrOut = #{inOrOut},creatTime = #{creatTime},confirmFlag=#{confirmFlag},remark = #{remark},sendStoreId = #{sendStoreId},sendStoreName = #{sendStoreName} where oddId = #{oddId}";
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
    @Results({
            @Result(property="listDetails",column="oddId",javaType=List.class,
                    many=@Many(select="org.ljx.dao.WarehouseRecordDetailMapper.listType")),
            @Result(property = "oddId",column = "oddId")
    })
    List<WarehouseRecord> listType(@Param("storeId") int storeId, @Param("type") byte type, @Param("beginTime")Timestamp beginTime,@Param("endTime")Timestamp endTime);

    static String buildList(@Param("storeId") int storeId,@Param("type") byte type, @Param("beginTime")Timestamp beginTime,@Param("endTime")Timestamp endTime) {
        return new SQL(){{
            SELECT("*");
            FROM("sys_warehouse_record");
            if (storeId != 0) {

                WHERE("(storeId = #{storeId} or sendStoreId = #{storeId})");
            }
            if (type != 0) {
                WHERE("type = #{type}");
            }
            if(beginTime!=null && !"".equals(beginTime)){
                WHERE("creatTime > #{beginTime}");
            }
            if(endTime!=null && !"".equals(endTime)){
                WHERE("creatTime < #{endTime}");
            }

            WHERE("status = 1");
            ORDER_BY(" creatTime desc");
        }}.toString();
    }

    @Select(FIND_SQL)
    @ResultType(WarehouseRecord.class)
    @Results({
            @Result(property="listDetails",column="oddId",javaType=List.class,
                    many=@Many(select="org.ljx.dao.WarehouseRecordDetailMapper.listType")),
            @Result(property = "oddId",column = "oddId")
    })
    WarehouseRecord findById(String oddId);
}
