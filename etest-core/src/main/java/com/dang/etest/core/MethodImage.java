package com.dang.etest.core;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dang.etest.entity.MethodContext;
import com.dang.etest.util.MethodContextUtil;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

/**
 * Description: 代理对象的属性中的方法拦截器
 * 为代理对象中 执行的成员变量的方法创建镜像缓存
 *
 * @Author dangqihe
 * @Date Create in 2018/4/22
 */
public class MethodImage implements MethodHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MethodImage.class);

    private Object targetObject;
    private Map<String, MethodContext> methodContextMap;
    private String useClassName;            //  镜像文件路径s

    private MethodImage(Object targetObject, String useClassName, Map<String, MethodContext> methodContextMap) {
        this.targetObject = targetObject;        // 设置代理对象
        this.useClassName = useClassName;
        this.methodContextMap = methodContextMap;
    }

    /**
     * 创建一个方法来完成创建代理对象
     *
     * @param targetObject 被代理对象
     * @param useClassName 使用类名
     *
     * @return 代理对象
     */
    static Object createInstance(Object targetObject, String useClassName, Map<String, MethodContext> methodContextMap)
            throws IllegalAccessException, InstantiationException {
        MethodImage methodImage = new MethodImage(targetObject, useClassName, methodContextMap);
        // 代理工厂
        ProxyFactory proxyFactory = new ProxyFactory();
        // 设置需要创建子类的父类
        proxyFactory.setSuperclass(targetObject.getClass());
        proxyFactory.setHandler(methodImage);
        return proxyFactory.createClass().newInstance();  //创建代理类对象.
    }

    /**
     * @param proxy       为由Javassist动态生成的代理类实例
     * @param method      当前要调用的方法
     * @param proxyMethod 为生成的代理类对方法的代理引用
     * @param args        参数值列表
     *
     * @return 从代理实例的方法调用返回的值
     *
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Method proxyMethod, Object[] args) throws Throwable {
        String key = MethodContextUtil.buildKey(method.getDeclaringClass().getName(), method.getName(), args);
        MethodContext context = methodContextMap.get(key);

        if (context != null) {    // 如果存在之前存储的执行结果 就重现结果
            if (context.getThrowable() != null) {
                throw context.getThrowable();
            }
            if (context.getResult() == null) {
                return null;
            }
            return JSON.parseObject(JSON.toJSONString(context.getResult()), context.getResultClass());
        }
        // 创建一个新的方法执行上下文
        context = new MethodContext(method, args);
        try {
            Object result = method.invoke(targetObject, args);
            if (result != null) {
                context.setResult(result);
                context.setResultClass(result.getClass());
            }
        } catch (Throwable throwable) {
            context.setThrowable(throwable);
            throw throwable;
        } finally {
            // 保存方法执行上下文
            methodContextMap.put(key, context);
        }
        return context.getResult();
    }
}