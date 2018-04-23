package com.dang.etest.core;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @Author dangqihe
 * @Date Create in 2018/4/22
 */
public class EtestFactory {

    private static final Logger LOG = LoggerFactory.getLogger(EtestFactory.class);

    public static <T> T proxy(T obj){
        Class<?> clazz = obj.getClass();
        String useClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        ProxyMethod proxyMethod = new ProxyMethod();
        obj = (T) proxyMethod.createInstance(obj);
        // 如果有父类  获取父类属性
        while (clazz != Object.class){
            // 获取实体类的所有属性，返回Field数组
            Field[] fields = clazz.getDeclaredFields();
            // 遍历所有属性
            for (Field field : fields) {
                try {
                    field.setAccessible(true);              // 设置为可访问的
                    Object fieldValue = field.get(obj);     // 获得该属性对应的对象
                    MethodImage mockMethod = new MethodImage();
                    if(field.getType().equals(String.class)){
                        continue;
                    }
                    Object proxy = mockMethod.createInstance(fieldValue,useClassName);
                    field.set(obj, proxy);      // 获得该属性为代理对象
                } catch (Throwable e) {
                    // class 为 static  不为改属性做代理
                    LOG.info("field create proxy error: " + field.getName(), e);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return obj;
    }


}
