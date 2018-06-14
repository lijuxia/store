package org.ljx.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.ljx.entity.Product;
import org.ljx.entity.WarehouseRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 产品数据层接口
 * Created by ljx on 2018/5/15.
 */
@Mapper
@Repository
public interface ProductMapper {

    final String INSERT_SQL = "insert into sys_product (name,code,unit,type,status)values(#{name},#{code},#{unit},#{type},#{status})";
    final String UPDATE_SQL = "update sys_product set name = #{name},code = #{code},unit = #{unit},type = #{type},status = #{status} where id = #{id}";
    final String SELECT_SQL = "select * from sys_product where status = 1 ";
    final String FIND_SQL = "select * from sys_product  where id = #{id}";
    final String DELETE_SQL = "delete from sys_product where id = #{id}";
    final String FIND_CODE_SQL = "select * from sys_product  where code = #{code}";

    @Insert(INSERT_SQL)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Product product);

    @Delete(DELETE_SQL)
    void delete(int id);

    @Update(UPDATE_SQL)
    void update(Product product);

    @SelectProvider(type = ProductMapper.class ,method = "buildList")
    @ResultType(Product.class)
    @Results({
            @Result(property="details",column="id",javaType=List.class,
                    many=@Many(select="org.ljx.dao.ProductDetailMapper.list")),
            @Result(property = "id",column = "id")
    })
    List<Product> list(@Param("types") byte[] types);

    static String buildList(@Param("types") byte[] types) {
        return new SQL(){{
            SELECT("*");
            FROM("sys_product");
            if (types != null && types.length > 0) {
                StringBuffer temp = new StringBuffer("(");
                for(int i=0;i<types.length;i++){
                    if(!"(".equals(temp.toString())){
                        temp.append(" or ");
                    }
                    temp.append(" type = "+types[i]);
                }
                temp.append(")");
                WHERE(temp.toString());
            }
            WHERE("status = 1");
        }}.toString();
    }


    @Select(FIND_SQL)
    @ResultType(Product.class)
    @Results({
            @Result(property="details",column="id",javaType=List.class,
                    many=@Many(select="org.ljx.dao.ProductDetailMapper.list")),
            @Result(property = "id",column = "id")
    })
    Product findById(int id);

    @Select(FIND_CODE_SQL)
    @ResultType(Product.class)
    Product findByCode(String code);
}
