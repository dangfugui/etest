package com.dang.etest.sample;

import org.junit.Assert;
import org.junit.BeforeClass;

import com.dang.etest.core.EtestFactory;

/**
 * Description:
 *
 * @Author dangqihe
 * @Date Create in 2018/4/22
 */
public class UserServiceTest {

    private static UserService userService = null;

    @BeforeClass
    public static void Before() throws IllegalAccessException {
        userService = new UserService();
        userService.setBeanName("testBean");
        userService = EtestFactory.proxy(userService);
    }

    @org.junit.Test
    public void getBeanName() throws Exception {
        User user = userService.getById(3);
        Assert.assertEquals("userName:"+3, user.getUserName());
    }
    @org.junit.Test
    public void getSuperName() throws Exception {
        Assert.assertEquals("super", userService.getName());
    }


    @org.junit.Test
    public void getByIdAndName() throws Exception {
        User user = userService.getByIdAndName(3,"dang");
        Assert.assertEquals("dang", user.getUserName());
    }

}
