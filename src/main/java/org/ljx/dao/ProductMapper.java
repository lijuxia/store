package org.ljx.dao;

import org.apache.ibatis.annotations.*;
import org.ljx.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Insert(INSERT_SQL)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Product sroduct);

    @Delete(DELETE_SQL)
    void delete(int id);

    @Update(UPDATE_SQL)
    void update(Product sroduct);

    @Select(SELECT_SQL)
    @ResultType(Product.class)
    List<Product> list();

    @Select(FIND_SQL)
    @ResultType(Product.class)
    Product findById(int id);
}
