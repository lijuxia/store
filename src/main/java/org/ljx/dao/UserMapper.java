package org.ljx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.ljx.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 产品数据层接口
 * @author pengfei.xiong
 * @date 2017年11月16日
 */
@Mapper
@Repository
public interface UserMapper {
    /**
     * 根据名称查询产品
     * @param name 名称
     * @return 返回产品实体对象
     */
    @Select(" SELECT * FROM sys_user WHERE username = #{name}")
    @ResultType(User.class)
    List<User> selectUserByName(@Param("name") String name);
}
