package org.ljx.dao;

import com.sun.tools.corba.se.idl.constExpr.Times;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.ljx.entity.Product;
import org.ljx.entity.Store;
import org.ljx.entity.Warehouse;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ljx on 2018/5/17.
 */
@Mapper
@Repository
public interface WarehouseMapper {

    final String INSERT_SQL = "insert into sys_warehouse (balance,storeId,productId,status,time)values(#{balance},#{storeId},#{productId},#{status},#{time})";
    final String UPDATE_SQL = "update sys_warehouse set balance = #{balance},storeId = #{storeId},productId = #{productId},status = #{status},time = #{time} where id = #{id}";
    final String SELECT_SQL = "select * from sys_warehouse where status = 1 ";
    final String FIND_SQL = "select * from sys_warehouse  where id = #{id}";
    final String DELETE_SQL = "delete from sys_warehouse where id = #{id}";
    final String FIND_STORE_PRODUCT_SQL = "select * from sys_warehouse  where storeId = #{storeId} and productId = #{productId} and status = 1";
    final String UPDATE_DURING_DATE_SQL = "update sys_warehouse set balance = balance +#{balance} where storeId = #{storeId} and productId = #{productId} and time>=#{beginTime} and time<=#{endTime}";

    @Insert(INSERT_SQL)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Warehouse warehouse);

    @Delete(DELETE_SQL)
    void delete(int id);

    @Update(UPDATE_SQL)
    void update(Warehouse warehouse);

    @Update(UPDATE_DURING_DATE_SQL)
    void updateHistory(@Param("storeId")int storeId, @Param("productId")int productId, @Param("balance")BigDecimal balance, @Param("beginTime")Timestamp beginTime,@Param("endTime")Timestamp endTime);

    @SelectProvider(type = WarehouseMapper.class ,method = "buildList")
    @ResultType(Warehouse.class)
    @Results({
            @Result(property="store",column="storeId",javaType=Store.class,
                    one=@One(select="org.ljx.dao.StoreMapper.findById")),
            @Result(property="product",column="productId",javaType=Product.class,
                    one=@One(select="org.ljx.dao.ProductMapper.findById")),
            @Result(property = "productId",column = "productId"),
            @Result(property = "storeId",column = "storeId")
    })
    List<Warehouse> list(@Param("storeId")int storeId, @Param("productId")int productId, @Param("status") byte status
            , @Param("beginDate")Timestamp beginDate, @Param("endDate")Timestamp endDate,@Param("oders")String oders);

    static String buildList(@Param("storeId")int storeId,@Param("productId")int productId,@Param("status") byte status
            , @Param("beginDate")Timestamp beginDate, @Param("endDate")Timestamp endDate,@Param("oders")String oders) {
        return new SQL(){{
            SELECT("*");
            FROM("sys_warehouse");
            if(storeId != 0){
                WHERE("storeId = #{storeId}");
            }
            if(productId != 0){
                WHERE("productId = #{productId}");
            }
            if (status != 0) {
                WHERE("status = #{status}");
            }
            if(beginDate!=null && !"".equals(beginDate)){
                WHERE("time >= #{beginDate}");
            }
            if(endDate!=null && !"".equals(endDate)){
                WHERE("time <= #{endDate}");
            }
            if(oders!=null && !"".equals(oders)){
                ORDER_BY(oders);
            }
        }}.toString();
    }

    @Select(FIND_SQL)
    @ResultType(Warehouse.class)
    Warehouse findById(int id);

    @Select(FIND_STORE_PRODUCT_SQL)
    @ResultType(Warehouse.class)
    Warehouse findByStoreIdAndProductId(@Param("storeId")int storeId,@Param("productId")int productId);

}
