package com.dang.etest.core;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Description: 代理对象的方法拦截器
 * 记录 调用记录  创建文档
 *
 * @Author dangqihe
 * @Date Create in 2018/4/23
 */
public class ProxyMethod implements MethodInterceptor {
    private Object targetObject;
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return method.invoke(targetObject,args);
    }

    //创建一个方法来完成创建代理对象
    Object createInstance(Object targetObject){
        this.targetObject = targetObject;   //  => 设置代理对象
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(   targetObject.getClass() );
        enhancer.setCallback(    this  );
        return enhancer.create();  //创建代理类对象.
    }
}
