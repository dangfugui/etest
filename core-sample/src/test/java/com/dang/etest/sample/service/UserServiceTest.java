package com.dang.etest.sample.service;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;

import com.dang.etest.core.EtestConfig;
import com.dang.etest.core.EtestFactory;
import com.dang.etest.sample.entity.User;

/**
 * Description:
 *
 * @Author dangqihe
 * @Date Create in 2018/4/22
 */
public class UserServiceTest {

    private static UserService userService = new UserService();

    @BeforeClass
    public static void Before() throws IllegalAccessException {
        EtestConfig.userDir = System.getProperty("user.dir") + "/core-sample";     // 测试对象的原文件在test文件夹中
        EtestConfig.changeImage = true;
        userService = EtestFactory.proxy(userService);
    }

    @org.junit.Test
    public void getById() throws Exception {
        User user = userService.getById(3);
        Assert.assertEquals("userName:" + 3, user.getUserName());
        userService.getById(3, user);
    }

    @org.junit.Test
    public void getSuperName() throws Exception {
        Assert.assertEquals("super", userService.getName());
    }

    @org.junit.Test
    public void getByIdAndName() throws Exception {
        User user = userService.getByIdAndName(3, "dang");
        Assert.assertEquals("dang", user.getUserName());
    }

    @org.junit.Test
    public void testToString() throws Exception {
        userService.toString();
    }

    @org.junit.Test
    public void doFinal() {
        String res = userService.doFinal();
        Assert.assertEquals("finalMethod", res);
    }

    @org.junit.Test
    public void list() {
        List<User> res = userService.list();
        for (int i = 0; i < res.size(); i++) {
            res.get(i).getUserName().equals("userName:" + i);
        }
    }

}
