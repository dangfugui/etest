package com.dang.etest.model;

import java.lang.reflect.Method;
import java.util.Map;

import com.dang.etest.entity.MethodCase;

/**
 * Description: 测试用例的读取接口
 *
 * @Author dangqihe
 * @Date Create in 2018/4/25
 */
public interface MethodCaseReader {

    /**
     * 生成文档
     * @param useClassName  调用本应用的className
     * @param docMethodMap  运行的方法
     */

    /**
     * 读取测试用例
     *
     * @param useClassName 调用本应用的className
     * @param docMethodMap 测试用例
     * @param targetObject 代理对象
     */
    void doReader(String useClassName, Map<Method, MethodCase> docMethodMap, Object targetObject) throws Exception;

}
