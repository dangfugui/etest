package com.dang.etest.sample.mapper;

import java.util.ArrayList;
import java.util.List;

import com.dang.etest.sample.entity.User;

/**
 * Description:
 *
 * @Author dangqihe
 * @Date Create in 2018/4/22
 */

public class UserMapper {

    public User getUserById(Integer id) {
        User user = new User();
        user.setId(id);
        user.setUserName("userName:" + id);
        user.setAge(id);
        return user;
    }

    /**
     * mapper 通过用户名和id 获取用户
     *
     * @param id   ID
     * @param name 用户名
     *
     * @return User
     */
    public User getByIdAndName(Integer id, String name) {
        User user = new User();
        user.setId(id);
        user.setUserName(name);
        user.setAge(id);
        return user;
    }

    public List<User> list() {
        List<User> list = new ArrayList<User>();
        for (int i = 0; i < 5; i++) {
            list.add(getUserById(i));
        }
        return list;
    }
}
