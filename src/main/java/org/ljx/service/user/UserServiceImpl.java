package org.ljx.service.user;
import org.ljx.entity.User;
import org.ljx.dao.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品业务层实现类
 * @author XSWL pengfei.xiong
 * @date 2017年11月16日
 */
@Service
@MapperScan("org.ljx.dao") //与dao层的@Mapper二选一写上即可(主要作用是扫包)
public class UserServiceImpl implements UserService{
    //依赖注入
    @Autowired
    UserMapper mapper;

    public List<User> queryUserByName(String name) {
        List<User> pro = mapper.selectUserByName(name);
        return pro;
    }
}
