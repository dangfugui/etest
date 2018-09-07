package com.dang.etest.entity;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dang.etest.util.DigestUtil;

/**
 * Description:
 *
 * @Author dangqihe
 * @Date Create in 2018/4/22
 */
public class MethodContext {

    private String key;
    private String className;
    private String method;
    private Object[] args;
    private String argsMD5;
    private Object result;
    private Class resultClass;
    private Throwable throwable;
    private Object T;

    public MethodContext() {

    }

    public MethodContext(String key, Method method, Object[] args) {
        this.key = key;
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
            sb.append(JSON.toJSONString(o)).append(",");
        }
        return DigestUtil.toMD5(sb.toString());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public void setResult(Object result) {
        if (result == null) {
            return;
        }
        if (result instanceof List && ((List) result).size() > 0) {
            this.resultClass = ((List) result).get(0).getClass();
        } else {
            this.resultClass = result.getClass();
        }
        this.result = result;
    }

    public Object doReturn(Class<?> returnType) {
        if (result == null) {
            return null;
        } else if (result.getClass() == returnType) {
            return result;
        }
        if (result instanceof JSONArray || result instanceof List) {
            if (List.class.isAssignableFrom(returnType) && ((List) result).size() > 0) {
                return JSON.parseArray(JSON.toJSONString(result), resultClass);
            } else if (returnType == Object.class) {
                return result;
            }
        } else if (result instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) result;
            if (Map.class.isAssignableFrom(returnType)) {
                return result;
            }
            try {
                return jsonObject.toJavaObject(resultClass);
            } catch (Throwable throwable) {
                return JSON.parseObject(JSON.toJSONString(jsonObject), resultClass);
            }
        }
        return JSON.parseObject(JSON.toJSONString(result), resultClass);
    }
}
