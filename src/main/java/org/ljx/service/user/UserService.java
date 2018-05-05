package org.ljx.service.user;

import org.ljx.entity.User;

import java.util.List;

/**
 * Created by ljx on 2018/5/2.
 */
public interface UserService {

    public List<User> queryUserByName(String name);
}
