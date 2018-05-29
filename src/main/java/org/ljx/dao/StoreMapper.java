package org.ljx.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.ljx.entity.Store;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 店铺数据层接口
 * Created by ljx on 2018/5/14.
 */
@Mapper
@Repository
public interface StoreMapper {

    final String INSERT_SQL = "insert into sys_store (name,username,password,type,status)values(#{name},#{username},#{password},#{type},#{status})";
    final String UPDATE_SQL = "update sys_store set name = #{name},username = #{username},password = #{password},type = #{type},status = #{status} where id = #{id}";
    final String SELECT_SQL = "select * from sys_store where status = 1";
    final String FIND_SQL = "select * from sys_store  where id = #{id}";
    final String DELETE_SQL = "delete from sys_store where id = #{id}";
    final String FIND_BY_USERNAME_SQL = "select * from sys_store where username = #{username}";
    @Insert(INSERT_SQL)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Store store);

    @Delete(DELETE_SQL)
    void delete(int id);

    @Update(UPDATE_SQL)
    void update(Store store);

    @Select(SELECT_SQL)
    @ResultType(Store.class)
    List<Store> list();

    @SelectProvider(type = StoreMapper.class, method = "buildList")
    @ResultType(Store.class)
    List<Store> listType(@Param("type") byte type);

    static String buildList(@Param("type") byte type) {
        return new SQL(){{
            SELECT("*");
            FROM("sys_store");
            if (type != 0) {
                WHERE("type = #{type}");
            }
            WHERE("status = 1");
        }}.toString();
    }

    @Select(FIND_SQL)
    @ResultType(Store.class)
    Store findById(int id);

    @Select(FIND_BY_USERNAME_SQL)
    @ResultType(Store.class)
    Store findByUsername(String username);
}
