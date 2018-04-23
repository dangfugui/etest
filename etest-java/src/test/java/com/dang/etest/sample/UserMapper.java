package com.dang.etest.sample;

/**
 * Description:
 *
 * @Author dangqihe
 * @Date Create in 2018/4/22
 */

public class UserMapper {

    public User getUserById(Integer id){
        User user = new User();
        user.setId(id);
        user.setUserName("userName:"+id);
        user.setAge(id);
        return user;
    }
}
