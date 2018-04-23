package com.dang.etest.core;

import java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;
import com.dang.etest.entity.EtestConfig;
import com.dang.etest.entity.MethodContext;
import com.dang.etest.util.MethodContextUtil;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Description: 代理对象的属性中的方法拦截器
 * 为代理对象中 执行的成员变量的方法创建镜像缓存
 *
 * @Author dangqihe
 * @Date Create in 2018/4/22
 */
public class MethodImage implements MethodInterceptor {

    private Object targetObject;
    private String path;            //  镜像文件路径

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        MethodContext context = MethodContextUtil.find(path, method, args);
        if (context != null) {    // 如果存在之前存储的执行结果 就重现结果
            if (context.getThrowable() != null) {
                throw context.getThrowable();
            }
            return JSON.parseObject(JSON.toJSONString(context.getResult()), context.getResultClass());
        }
        // 创建一个新的方法执行上下文
        context = new MethodContext(method, args);
        try {
            Object result = methodProxy.invoke(targetObject, args);
            context.setResult(result);
            context.setResultClass(result.getClass());
        } catch (Throwable throwable) {
            context.setThrowable(throwable);
        }
        // 保存方法执行上下文
        MethodContextUtil.save(path, context);
        return context.getResult();
    }

    //创建一个方法来完成创建代理对象
    static Object createInstance(Object targetObject, String useClassName) {
        MethodImage methodImage = new MethodImage();
        methodImage.path = EtestConfig.methodImageDir + useClassName.replaceAll("\\.", "/") + ".image";
        methodImage.targetObject = targetObject;   //  => 设置代理对象
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetObject.getClass());
        // enhancer.setClassLoader(   targetObject.getClass().getClassLoader()  );
        enhancer.setCallback(methodImage);
        return enhancer.create();  //创建代理类对象.
    }

}
