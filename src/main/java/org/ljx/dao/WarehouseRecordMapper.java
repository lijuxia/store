package org.ljx.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.ljx.entity.Product;
import org.ljx.entity.WarehouseRecord;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ljx on 2018/5/17.
 */
@Mapper
@Repository
public interface WarehouseRecordMapper {

    final String INSERT_SQL = "insert into sys_warehouse_record (oddId,storeId,storeName,type,status,inOrOut,creatTime,confirmFlag,remark,sendStoreId,sendStoreName,date,makeProductId,makeNum)values(#{oddId},#{storeId},#{storeName},#{type},#{status},#{inOrOut},#{creatTime},#{confirmFlag},#{remark},#{sendStoreId},#{sendStoreName},#{date},#{makeProductId},#{makeNum})";
    final String UPDATE_SQL = "update sys_warehouse_record set storeId = #{storeId},storeName = #{storeName},type = #{type},status = #{status},inOrOut = #{inOrOut},creatTime = #{creatTime},confirmFlag=#{confirmFlag},remark = #{remark},sendStoreId = #{sendStoreId},sendStoreName = #{sendStoreName},date = #{date},makeProductId = #{makeProductId},makeNum = #{makeNum} where oddId = #{oddId}";
    final String SELECT_SQL = "select * from sys_warehouse_record where status = 1";
    final String FIND_SQL = "select * from sys_warehouse_record  where oddId = #{oddId}";
    final String DELETE_SQL = "delete from sys_warehouse_record where oddId = #{oddId}";

    @Insert(INSERT_SQL)
    void insert(WarehouseRecord warehouseRecord);

    @Delete(DELETE_SQL)
    void delete(int id);

    @Update(UPDATE_SQL)
    void update(WarehouseRecord warehouseRecord);

    @SelectProvider(type = WarehouseRecordMapper.class, method = "buildList")
    @ResultType(WarehouseRecord.class)
    @Results({
            @Result(property="listDetails",column="oddId",javaType=List.class,
                    many=@Many(select="org.ljx.dao.WarehouseRecordDetailMapper.list")),
            @Result(property = "oddId",column = "oddId"),
            @Result(property = "makeProduct",column = "makeProductId",javaType = Product.class,
                    one=@One(select = "org.ljx.dao.ProductMapper.findById")),
            @Result(property = "makeProductId",column = "makeProductId")
    })
    List<WarehouseRecord> list(@Param("storeId") int storeId, @Param("types") byte[] types
            , @Param("beginTime")Timestamp beginTime,@Param("endTime")Timestamp endTime
            , @Param("beginDate")Date beginDate, @Param("endDate")Date endDate
            ,@Param("confirmFlag") byte confirmFlag, @Param("sendStoreId") Integer sendStoreId, @Param("order")String order);

    static String buildList(@Param("storeId") int storeId,@Param("types") byte[] types
            , @Param("beginTime")Timestamp beginTime,@Param("endTime")Timestamp endTime
            , @Param("beginDate")Date beginDate, @Param("endDate")Date endDate
            ,@Param("confirmFlag") byte confirmFlag, @Param("sendStoreId") Integer sendStoreId,@Param("order")String order) {
        return new SQL(){{
            SELECT("*");
            FROM("sys_warehouse_record");
            if (storeId != 0) {
                WHERE("(storeId = #{storeId} or sendStoreId = #{storeId})");
            }
            if (types != null && types.length > 0) {
                StringBuffer typeStr = new StringBuffer();
                typeStr.append("(");
                for(int i=0;i<types.length;i++){
                    byte type = types[i];
                    if(i>0){
                        typeStr.append(" or ");
                    }
                    typeStr.append("type = '"+type+"'");
                }
                typeStr.append(")");
                WHERE(typeStr.toString());
            }
            if(beginTime!=null && !"".equals(beginTime)){
                WHERE("creatTime > #{beginTime}");
            }
            if(endTime!=null && !"".equals(endTime)){
                WHERE("creatTime < #{endTime}");
            }
            if(beginDate!=null && !"".equals(beginDate)){
                WHERE("date >= #{beginDate}");
            }
            if(endDate!=null && !"".equals(endDate)){
                WHERE("date <= #{endDate}");
            }
            if(sendStoreId != null && sendStoreId > 0){
                WHERE("sendStoreId = #{sendStoreId}");
            }
            if(confirmFlag>0){
                WHERE("confirmFlag = #{confirmFlag}");
            }
            WHERE("status = 1");
            ORDER_BY(order);
        }}.toString();
    }

    @Select(FIND_SQL)
    @ResultType(WarehouseRecord.class)
    @Results({
            @Result(property="listDetails",column="oddId",javaType=List.class,
                    many=@Many(select="org.ljx.dao.WarehouseRecordDetailMapper.list")),
            @Result(property = "oddId",column = "oddId"),
            @Result(property = "makeProduct",column = "makeProductId",javaType = Product.class,
                    one=@One(select = "org.ljx.dao.ProductMapper.findById")),
            @Result(property = "makeProductId",column = "makeProductId")
    })
    WarehouseRecord findById(String oddId);

    @SelectProvider(type = WarehouseRecordMapper.class, method = "buildFindByDate")
    @ResultType(WarehouseRecord.class)
    @Results({
            @Result(property="listDetails",column="oddId",javaType=List.class,
                    many=@Many(select="org.ljx.dao.WarehouseRecordDetailMapper.list")),
            @Result(property = "oddId",column = "oddId"),
    })
    List<WarehouseRecord> findByDate(@Param("storeId") int storeId
            , @Param("date")Date date, @Param("order")String order);

    static String buildFindByDate(@Param("storeId") int storeId
            , @Param("date")Date date, @Param("order")String order) {
        return new SQL(){{
            SELECT("*");
            FROM("sys_warehouse_record ");
            if (storeId != 0) {
                WHERE("storeId = #{storeId}");
            }
            if(date!=null && !"".equals(date)){
                WHERE("date = #{date}");
            }
            WHERE("type = "+WarehouseRecord.TYPE_CHECK);
            WHERE("status = 1");
            ORDER_BY(order);
        }}.toString();
    }
}
