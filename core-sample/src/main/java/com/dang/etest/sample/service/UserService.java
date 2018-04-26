package com.dang.etest.sample.service;

import com.dang.etest.sample.entity.User;
import com.dang.etest.sample.mapper.UserMapper;

/**
 * Description: 用户服务类
 *
 * @Author dangqihe
 * @Date Create in 2018/4/22
 */
public class UserService extends UserServiceSuper {

    private UserMapper userMapper = new UserMapper();
    private String beanName = "";

    /**
     * 通过ID获取用户信息
     *
     * @param id 用户ID
     *
     * @return 用户对象
     */
    public User getById(Integer id) {
        return userMapper.getUserById(id);
    }

    /**
     * 测试重载方法
     *
     * @param id   id
     * @param user userBean
     *
     * @return userBean
     */
    public User getById(Integer id, User user) {
        return userMapper.getUserById(id);
    }

    /**
     * 通过ID和用户名获取用户
     *
     * @param id   用户ID
     * @param name 用户对象
     *
     * @return UserBean
     */
    public User getByIdAndName(Integer id, String name) {
        return userMapper.getByIdAndName(id, name);
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
