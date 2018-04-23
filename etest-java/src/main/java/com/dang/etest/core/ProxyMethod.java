package com.dang.etest.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dang.etest.entity.DocMethod;
import com.dang.etest.entity.EtestConfig;
import com.dang.etest.util.DocUtil;

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
                    DocUtil.createDoc(path,docMethodMap);
                    LOG.info("ShutdownHook");
                } catch (IOException e) {
                    LOG.error("create doc error",e);
                }
            }
        }));
    }


    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        DocMethod docMethod = docMethodMap.get(method);
        if(docMethod == null){
            docMethod = new DocMethod();
        }
        Object result = method.invoke(targetObject, args);
        docMethod.addCase(args,result);
        docMethodMap.put(method,docMethod);
        return result;
    }

    //创建一个方法来完成创建代理对象
    static Object createInstance(Object targetObject,String useClassName){
        ProxyMethod proxyMethod = new ProxyMethod();
        proxyMethod.path = EtestConfig.docDir + useClassName.replaceAll("\\.","/") + ".md";
        proxyMethod.targetObject = targetObject;   //  => 设置代理对象
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(   targetObject.getClass() );
        enhancer.setCallback(proxyMethod);
        return enhancer.create();  //创建代理类对象.
    }

}
