package com.dang.etest.model;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dang.etest.core.EtestConfig;
import com.dang.etest.entity.MethodCase;
import com.dang.etest.util.Classes;
import com.dang.etest.util.FileUtil;
import com.dang.etest.util.JavaDocReader;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;

/**
 * Description:
 *
 * @Author dangqihe
 * @Date Create in 2018/4/25
 */
public class MarkdownCreater implements MethodCaseReader {

    private static final Logger LOG = LoggerFactory.getLogger(MarkdownCreater.class);
    private static final String ENTER = "\r\n";

    /**
     * 添加参数内容
     *
     * @param sb     StringBuffer
     * @param method 方法
     */
    private static void writerMethod(StringBuffer sb, Method method, MethodDoc methodDoc) {
        sb.append(ENTER).append("##" + method.getName());
        if (methodDoc != null) {
            sb.append("    ").append(methodDoc.commentText());
        }
        sb.append(ENTER);
        Parameter[] parameters = method.getParameters();
        if (parameters != null && parameters.length > 0) {
            sb.append("** Method parameters ：**").append(ENTER).append(ENTER);
            sb.append("| 参数名称 | 必填 |  类型 | 说明 |").append(ENTER);
            sb.append("| -------- | ---- | ----- | ----- |").append(ENTER);
            try {
                String[] argsNames = Classes.getMethodParamNames(method.getDeclaringClass(), method);
                for (int i = 0; i < parameters.length; i++) {
                    ParamTag paramTag = JavaDocReader.getParamTag(methodDoc, argsNames[i]);
                    sb.append(String.format("| %s | %s | %s | %s |", argsNames[i], " ",
                            parameters[i].getType().getSimpleName(),
                            paramTag == null ? "" : paramTag.parameterComment())).append(ENTER);
                }
            } catch (Exception e) {
                LOG.error("MethodParamNames error", e);
            }
        }
    }

    /**
     * 添加示例内容
     *
     * @param sb         StringBuffer
     * @param methodCase test case
     */
    private static void writerExamples(StringBuffer sb, MethodCase methodCase, MethodDoc methodDoc) {
        sb.append("** Method Examples ：**").append(ENTER);
        for (MethodCase.Case c : methodCase.getCaseList()) {
            if (c.getArgs() != null && c.getArgs().length > 0) {
                sb.append("| 参数名称 | 示例 |  类型 | 说明 |").append(ENTER);
                sb.append("| -------- | ---- | ----- | ----- |").append(ENTER);
                try {
                    String[] argsNames = Classes.getMethodParamNames(methodCase.getMethod().getDeclaringClass(),
                            methodCase.getMethod());
                    Parameter[] parameters = methodCase.getMethod().getParameters();
                    for (int i = 0; i < parameters.length; i++) {
                        ParamTag paramTag = JavaDocReader.getParamTag(methodDoc, argsNames[i]);
                        sb.append(String.format("| %s | %s | %s | %s |", argsNames[i],
                                JSON.toJSONString(c.getArgs()[i]),
                                parameters[i].getType().getSimpleName(),
                                paramTag == null ? "" : paramTag.parameterComment())).append(ENTER);
                    }
                } catch (Exception e) {
                    LOG.error("MethodParamNames error", e);
                }
            }
            sb.append("```json").append(ENTER);
            sb.append(JSON.toJSONString(c.getResult(), true)).append(ENTER).append("```").append(ENTER);
        }
    }

    @Override
    public void doReader(String useClassName, Map<Method, MethodCase> docMethodMap, Object targetObject)
            throws Exception {
        String path = EtestConfig.userDir + EtestConfig.docDir + useClassName.replaceAll("\\.", "/") + ".md";
        File file = new File(path);
        if (!file.exists()) {
            FileUtil.createFile(path);
        }
        JavaDocReader docReader = JavaDocReader.newInstance(targetObject);
        StringBuffer sb = new StringBuffer();
        sb.append(ENTER).append("[TOC]").append(ENTER);
        sb.append("#").append(targetObject.getClass().getSimpleName());
        if (docReader != null) {
            sb.append("    ").append(docReader.getClassDoc()
                    .commentText().replaceAll("Description", "")).append(ENTER)
                    .append(docReader.getClassDoc().getRawCommentText());
        } else {
            LOG.error("class source not find:");
        }
        sb.append(ENTER);
        for (Map.Entry<Method, MethodCase> entry : docMethodMap.entrySet()) {
            Method method = entry.getKey();
            MethodDoc methodDoc = docReader == null ? null : docReader.getMethodDoc(method);
            writerMethod(sb, method, methodDoc);
            writerExamples(sb, entry.getValue(), methodDoc);
        }
        FileUtil.write(file, false, sb.toString());
    }
}


