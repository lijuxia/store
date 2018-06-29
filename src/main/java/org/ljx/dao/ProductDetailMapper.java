package org.ljx.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.ljx.entity.Product;
import org.ljx.entity.ProductDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ljx on 2018/6/9.
 */
@Mapper
@Repository
public interface ProductDetailMapper {

    final String INSERT_SQL = "insert into sys_product_detail (productId,detailId,num,status)values(#{productId},#{detailId},#{num},#{status})";
    final String UPDATE_SQL = "update sys_product_detail set productId = #{productId},detailId = #{detailId},num = #{num},status = #{status} where id = #{id}";
    final String SELECT_SQL = "select * from sys_product_detail where status = 1 ";
    final String DELETE_SQL = "delete from sys_product_detail where id = #{id}";

    @Insert(INSERT_SQL)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ProductDetail productDetail);

    @Delete(DELETE_SQL)
    void delete(int id);

    @Update(UPDATE_SQL)
    void update(ProductDetail productDetail);

    @SelectProvider(type = ProductDetailMapper.class ,method = "buildList")
    @ResultType(ProductDetail.class)
    @Results({
            @Result(property="detail",column="detailId",javaType=Product.class,
                    one=@One(select="org.ljx.dao.ProductMapper.findById")),
            @Result(property = "detailId",column = "detailId"),
            @Result(property = "id",column = "id")
    })
    List<ProductDetail> list(@Param("id") Integer id);

    static String buildList(@Param("id") Integer id) {
        return new SQL(){{
            SELECT("*");
            FROM("sys_product_detail");
            if (id != null) {
                WHERE("productId = #{id}");
            }
            WHERE("status = 1");
        }}.toString();
    }

}
