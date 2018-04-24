package com.dang.etest.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dang.etest.entity.DocMethod;
import com.dang.etest.entity.EtestConfig;
import com.dang.etest.util.DocUtil;;import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

/**
 * Description: 代理对象的方法拦截器
 * 记录 调用记录  创建文档
 *
 * @Author dangqihe
 * @Date Create in 2018/4/23
 */
public class ProxyMethod implements MethodHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyMethod.class);
    private Object targetObject;
    private String path;            //   文档文件路径
    private Map<Method,DocMethod> docMethodMap = new HashMap<>();

    private ProxyMethod(){
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    DocUtil.createDoc(path,docMethodMap,targetObject);
                    LOG.info("ShutdownHook");
                } catch (IOException e) {
                    LOG.error("create doc error",e);
                }
            }
        }));
    }



    //创建一个方法来完成创建代理对象
    static Object createInstance(Object targetObject,String useClassName)
            throws IllegalAccessException, InstantiationException {
        ProxyMethod proxyMethod = new ProxyMethod();
        proxyMethod.path = EtestConfig.docDir + useClassName.replaceAll("\\.","/") + ".md";
        proxyMethod.targetObject = targetObject;   //  => 设置代理对象
        // 代理工厂
        ProxyFactory proxyFactory = new ProxyFactory();
        // 设置需要创建子类的父类
        proxyFactory.setSuperclass(targetObject.getClass());
        proxyFactory.setHandler(proxyMethod);
        return proxyFactory.createClass().newInstance();  //创建代理类对象.
    }

    /**
     *
     * @param proxy         为由Javassist动态生成的代理类实例
     * @param method        当前要调用的方法
     * @param proxyMethod   为生成的代理类对方法的代理引用
     * @param args          参数值列表
     * @return              从代理实例的方法调用返回的值
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Method proxyMethod, Object[] args) throws Throwable {
        DocMethod docMethod = docMethodMap.get(method);
        if(docMethod == null){
            docMethod = new DocMethod();
            docMethod.setMethod(method);
        }
        Object result = method.invoke(targetObject, args);
        docMethod.addCase(args,result);
        docMethodMap.put(method,docMethod);
        return result;
    }
}
