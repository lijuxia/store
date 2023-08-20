package org.ljx.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.ljx.entity.Product;
import org.ljx.entity.WarehouseRecordDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ljx on 2018/5/17.
 */
@Mapper
@Repository
public interface WarehouseRecordDetailMapper {

    final String INSERT_SQL = "insert into sys_warehouse_record_detail (uuid,oddId,productId,num,productName,unit,beforeSaveNum)values(#{uuid},#{oddId},#{productId},#{num},#{productName},#{unit},#{beforeSaveNum})";
    final String UPDATE_SQL = "update sys_warehouse_record_detail set oddId = #{oddId},productId = #{productId},num = #{num},productName = #{productName},unit = #{unit},beforeSaveNum = #{beforeSaveNum} where uuid = #{uuid}";
    final String SELECT_SQL = "select * from sys_warehouse_record_detail";
    final String FIND_SQL = "select * from sys_warehouse_record_detail  where uuid = #{uuid}";
    final String DELETE_SQL = "delete from sys_warehouse_record_detail where uuid = #{uuid}";
    final String DELETE_BY_ODDID_SQL = "delete from sys_warehouse_record_detail where oddId = #{oddId}";

    @Insert(INSERT_SQL)
    void insert(WarehouseRecordDetail warehouseRecordDetail);

    @Delete(DELETE_SQL)
    void delete(String uuid);

    @Delete(DELETE_BY_ODDID_SQL)
    void deleteByOddId(String oddId);

    @Update(UPDATE_SQL)
    void update(WarehouseRecordDetail warehouseRecordDetail);

    @SelectProvider(type = WarehouseRecordDetailMapper.class, method = "buildList")
    @ResultType(WarehouseRecordDetail.class)
    @Results({
            @Result(property="product",column="productId",javaType=Product.class,
                    one=@One(select="org.ljx.dao.ProductMapper.findById")),
            @Result(property = "productId",column = "productId")
    })
    List<WarehouseRecordDetail> list(@Param("oddId") String oddId);

    static String buildList(@Param("oddId") String oddId) {
        return new SQL(){{
            SELECT("*");
            FROM("sys_warehouse_record_detail");
            if (oddId !=null && !"".equals(oddId)) {
                WHERE("oddId = #{oddId}");
            }
        }}.toString();
    }

    @Select(FIND_SQL)
    @ResultType(WarehouseRecordDetail.class)
    WarehouseRecordDetail findById(String uuid);
}
