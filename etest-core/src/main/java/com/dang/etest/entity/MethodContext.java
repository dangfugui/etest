package com.dang.etest.entity;

import java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;
import com.dang.etest.util.DigestUtil;

/**
 * Description:
 *
 * @Author dangqihe
 * @Date Create in 2018/4/22
 */
public class MethodContext {

    private String className;
    private String method;
    private Object[] args;
    private String argsMD5;
    private Object result;
    private Class resultClass;
    private Throwable throwable;

    public MethodContext() {

    }

    public MethodContext(Method method, Object[] args) {
        this.method = method.getName();
        this.className = method.getDeclaringClass().getName();
        this.args = args;
        this.argsMD5 = argsMd5(args);
    }

    public static String argsMd5(Object[] args) {
        if (args == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (Object o : args) {
            sb.append(JSON.toJSONString(0)).append(",");
        }
        return DigestUtil.toMD5(sb.toString());
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Class getResultClass() {
        return resultClass;
    }

    public void setResultClass(Class resultClass) {
        this.resultClass = resultClass;
    }

    public String getArgsMD5() {
        return argsMD5;
    }

    public void setArgsMD5(String argsMD5) {
        this.argsMD5 = argsMD5;
    }
}
