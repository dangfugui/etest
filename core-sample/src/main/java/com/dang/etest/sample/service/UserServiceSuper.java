package com.dang.etest.sample.service;

import com.dang.etest.sample.mapper.UserMapper;

/**
 * Description:
 *
 * @Author dangqihe
 * @Date Create in 2018/4/22
 */
public class UserServiceSuper {

    private UserMapper supperMapper = new UserMapper();
    private String name = "super";

    public String getName() {
        return name;
    }
}
