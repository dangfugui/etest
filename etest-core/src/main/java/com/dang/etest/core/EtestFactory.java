package com.dang.etest.core;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dang.etest.util.MethodCases;
import com.dang.etest.util.MethodContexts;

/**
 * Description: etest  代理工厂
 *
 * @Author dangqihe
 * @Date Create in 2018/4/22
 */
public class EtestFactory {

    private static final Logger LOG = LoggerFactory.getLogger(EtestFactory.class);

    public static <T> T proxy(T obj) {
        obj = getTarget(obj);
        Class<?> clazz = obj.getClass();
        while (clazz.getName().contains("$")) {
            clazz = clazz.getSuperclass();
        }
        String useClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        // 如果有父类  获取父类属性
        while (clazz != Object.class) {
            // 获取实体类的所有属性，返回Field数组
            Field[] fields = clazz.getDeclaredFields();
            // 遍历所有属性
            for (Field field : fields) {
                if (skipFild(field)) {
                    continue;
                }
                try {
                    field.setAccessible(true);              // 设置为可访问的
                    Object fieldValue = field.get(obj);     // 获得该属性对应的对象
                    if (fieldValue == null) {
                        continue;
                    }
                    Object proxy = MethodImage.createInstance(fieldValue, useClassName,
                            MethodContexts.readMethodContextMap(useClassName, obj));
                    field.set(obj, proxy);      // 获得该属性为代理对象
                    LOG.info("mock:" + field.getType());
                } catch (Throwable e) {
                    // class 为 static  不为改属性做代理
                    LOG.error("field create proxy error: " + field.getName(), e);
                }
            }
            clazz = clazz.getSuperclass();
        }
        try {
            obj = (T) ProxyMethod.createInstance(obj, useClassName,
                    MethodCases.readerMethodMap(useClassName, obj));     // 返回代理对象
        } catch (Exception e) {
            return obj;
        }
        return obj;
    }

    private static boolean skipFild(Field field) {
        if (field.getType().getName().startsWith("java.lang")) {
            return true;
        } else if (field.getType().equals(int.class)) {
            return true;
        } else if (field.getType().equals(double.class)) {
            return true;
        } else if (field.getType().equals(float.class)) {
            return true;
        } else if (field.getType().equals(boolean.class)) {
            return true;
        } else if (field.getType().equals(long.class)) {
            return true;
        } else {
            return false;
        }
    }

    public static <T> T getTarget(T beanInstance) {
        try {
            Field gclib = beanInstance.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            if (gclib == null) {
                return beanInstance;
            }
            gclib.setAccessible(true);
            Object gclibValue = gclib.get(beanInstance);
            if (gclibValue == null) {
                return beanInstance;
            }
            Field advised = gclibValue.getClass().getDeclaredField("advised");
            if (advised == null) {
                return beanInstance;
            }
            advised.setAccessible(true);
            Object advisedValue = advised.get(gclibValue);
            if (advisedValue == null) {
                return beanInstance;
            }
            Field targetSource = advisedValue.getClass().getSuperclass().getSuperclass().getDeclaredField
                    ("targetSource");
            if (targetSource == null) {
                return beanInstance;
            }
            targetSource.setAccessible(true);
            Object target = targetSource.get(advisedValue);
            if (target == null) {
                return beanInstance;
            }
            Field targetField = target.getClass().getDeclaredField("target");
            if (targetField == null) {
                return beanInstance;
            }
            targetField.setAccessible(true);
            Object instance = targetField.get(target);
            if (instance != null) {
                return (T) instance;
            }
        } catch (Throwable e) {
        }
        return beanInstance;
    }
}
