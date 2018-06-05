package org.ljx.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.ljx.entity.Product;
import org.ljx.entity.Store;
import org.ljx.entity.Warehouse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ljx on 2018/5/17.
 */
@Mapper
@Repository
public interface WarehouseMapper {

    final String INSERT_SQL = "insert into sys_warehouse (balance,storeId,productId,status)values(#{balance},#{storeId},#{productId},#{status})";
    final String UPDATE_SQL = "update sys_warehouse set balance = #{balance},storeId = #{storeId},productId = #{productId},status = #{status} where id = #{id}";
    final String SELECT_SQL = "select * from sys_warehouse where status = 1 ";
    final String FIND_SQL = "select * from sys_warehouse  where id = #{id}";
    final String DELETE_SQL = "delete from sys_warehouse where id = #{id}";

    @Insert(INSERT_SQL)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Warehouse warehouse);

    @Delete(DELETE_SQL)
    void delete(int id);

    @Update(UPDATE_SQL)
    void update(Warehouse warehouse);

    @SelectProvider(type = WarehouseMapper.class ,method = "buildList")
    @ResultType(Warehouse.class)
    @Results({
            @Result(property="store",column="storeId",javaType=Store.class,
                    one=@One(select="org.ljx.dao.StoreMapper.findById")),
            @Result(property="product",column="productId",javaType=Product.class,
                    one=@One(select="org.ljx.dao.ProductMapper.findById"))
    })
    List<Warehouse> list(@Param("storeId")int storeId,@Param("status") byte status);

    static String buildList(@Param("storeId")int storeId,@Param("status") byte status) {
        return new SQL(){{
            SELECT("*");
            FROM("sys_warehouse");
            if(storeId != 0){
                WHERE("storeId = #{storeId}");
            }
            if (status != 0) {
                WHERE("status = #{status}");
            }
        }}.toString();
    }

    @Select(FIND_SQL)
    @ResultType(Warehouse.class)
    Warehouse findById(int id);

}
