package com.dang.etest.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dang.etest.entity.DocMethod;
import com.dang.etest.entity.EtestConfig;
import com.sun.javadoc.MethodDoc;

/**
 * Description: 文档 工具类
 *
 * @Author dangqihe
 * @Date Create in 2018/4/23
 */
public class DocUtil {

    private static Logger LOG = LoggerFactory.getLogger(DocUtil.class);
    private static final String ENTER = "\r\n";

    /**
     * 生成文档
     * @param path          文档路径
     * @param docMethodMap  运行的方法
     */
    public static void createDoc(String path, Map<Method, DocMethod> docMethodMap,Object targetObject) throws IOException {
        File file = new File(path);
        if(!file.exists()) {
            FileUtil.createFile(path);
        }
        JavaDocReader docReader = JavaDocReader.newInstance(EtestConfig.srcDir + targetObject.getClass().getName()
                .replaceAll("\\.","/")+".java");
        StringBuffer sb = new StringBuffer();
        sb.append(ENTER).append("[TOC]").append(ENTER);
        sb.append("#").append(targetObject.getClass().getSimpleName()).append("    ").append(docReader.getClassDoc()
                .commentText().replaceAll("Description","")).append(ENTER);
        sb.append(docReader.getClassDoc().getRawCommentText()).append(ENTER);
        for(Map.Entry<Method, DocMethod> entry : docMethodMap.entrySet()){
            Method method = entry.getKey();
            writerMethod(sb,method,docReader.getMethodDoc(method));
            writerExamples(sb,entry.getValue());
        }
        FileUtil.write(file,false,sb.toString());
    }

    /**
     * 添加参数内容
     * @param sb StringBuffer
     * @param method 方法
     */
    private static void writerMethod(StringBuffer sb, Method method, MethodDoc methodDoc) {
        sb.append(ENTER).append("##"+method.getName()).append(ENTER);
        Parameter[] parameters = method.getParameters();
        if(parameters != null && parameters.length > 0) {
            sb.append("** Method parameters ：**").append(ENTER).append(ENTER);
            sb.append("| 参数名称 | 必填 |  类型 | 说明 |").append(ENTER);
            sb.append("| -------- | ---- | ----- | ----- |").append(ENTER);
            try {
                String[] argsNames = Classes.getMethodParamNames(method.getDeclaringClass(), method);
                for(int i = 0; i < parameters.length ; i++){
                    sb.append(String.format("| %s | %s | %s | %s |",argsNames[i]," ",
                            parameters[i].getType().getSimpleName()," ")).append(ENTER);
                }
            } catch (Exception e) {
                LOG.error("MethodParamNames error",e);
            }
        }
    }

    /**
     * 添加示例内容
     * @param sb            StringBuffer
     * @param docMethod     test case
     */
    private static void writerExamples(StringBuffer sb, DocMethod docMethod) {
        sb.append("** Method Examples ：**").append(ENTER);
        for(DocMethod.Case c : docMethod.getCaseList()){
            if(c.getArgs() != null && c.getArgs().length > 0) {
                sb.append("| 参数名称 | 示例 |  类型 | 说明 |").append(ENTER);
                sb.append("| -------- | ---- | ----- | ----- |").append(ENTER);
                try {
                    String[] argsNames = Classes.getMethodParamNames(docMethod.getMethod().getDeclaringClass(),
                            docMethod.getMethod());
                    Parameter[] parameters = docMethod.getMethod().getParameters();
                    for (int i = 0; i < parameters.length; i++) {
                        sb.append(String.format("| %s | %s | %s | %s |", argsNames[i], c.getArgs()[i],
                                parameters[i].getType().getSimpleName(), " ")).append(ENTER);
                    }
                } catch (Exception e) {
                    LOG.error("MethodParamNames error", e);
                }
            }
            sb.append("```json").append(ENTER);
            sb.append(JSON.toJSONString(c.getResult())).append(ENTER).append("```").append(ENTER);
        }
    }

}
