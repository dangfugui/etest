package com.dang.etest.sample;

/**
 * Description:
 *
 * @Author dangqihe
 * @Date Create in 2018/4/22
 */
public class UserService extends UserServiceSuper{

    private UserMapper userMapper = new UserMapper();
    private String beanName = "";

    public User getById(Integer id){
        return userMapper.getUserById(id);
    }



    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
