package com.dang.etest.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dang.etest.entity.DocMethod;

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
    public static void createDoc(String path, Map<Method, DocMethod> docMethodMap) throws IOException {
        File file = new File(path);
        if(!file.exists()) {
            FileUtil.createFile(path);
        }
        for(Map.Entry<Method, DocMethod> entry : docMethodMap.entrySet()){
            writerMethod(file,entry.getKey());
        }
    }

    private static void writerMethod(File file, Method method) {
        StringBuffer sb = new StringBuffer();
        sb.append("##"+method.getName()).append(ENTER);
        if(method.getParameterTypes() != null) {
            sb.append("** Method parameters ：**").append(ENTER);
            sb.append("| 参数名称 | 必填 |  类型 | 说明 |").append(ENTER);
            sb.append("| -------- | ---- | ----- | ----- |").append(ENTER);
            try {
                String[] aa = Classes.getMethodParamNames(method.getDeclaringClass(), method);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(Class<?> a : method.getParameterTypes()) {
                sb.append("|").append(a.getSimpleName()).append("|").append("  ").append("|").append(a.getTypeName()).append("|  |");
            }
            //        sb.append("| ").append()
        }
        try {
            FileUtil.write(file,false,sb.toString());
        } catch (IOException e) {
            LOG.error("writerMethod error",e);
        }

    }
}
