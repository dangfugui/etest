package com.dang.etest.core;

import java.lang.reflect.Method;

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
    private Object [] args;
    private String argsMd5 = "";
    private Object result;
    private Class resultClass;
    private Throwable throwable;

    public MethodContext(){

    }

    public MethodContext(Method method, Object[] args) {
        this.method = method.getName();
        this.className = method.getDeclaringClass().getName();
        this.args = args;
        if(args != null){
            StringBuffer stringBuffer = new StringBuffer();
            for(Object arg:args){
                stringBuffer.append(arg);
            }
            argsMd5 = DigestUtil.toMD5(stringBuffer.toString());
        }
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

    public String getArgsMd5() {
        return argsMd5;
    }

    public void setArgsMd5(String argsMd5) {
        this.argsMd5 = argsMd5;
    }

    public Class getResultClass() {
        return resultClass;
    }

    public void setResultClass(Class resultClass) {
        this.resultClass = resultClass;
    }
}
