package com.dang.etest.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.dang.etest.util.FileUtil;

/**
 * Description:
 *
 * @Author dangqihe dangqihe
 * @Date Create in 2018/4/22
 */
public class MethodContextUtil {

    static MethodContext find(String path, Method method, Object args) {
        File  file= new File(path);
        if(file == null){
            return null;
        }
        try {
            List<String> list = FileUtil.readAsList(file);
            for (String line: list){
                MethodContext context = JSON.parseObject(line, MethodContext.class);
                if(context.getMethod().equals(method.getName()) &&
                        context.getClassName().equals(method.getDeclaringClass().getName())){
                    return context;
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    static void save(String path, MethodContext context) throws IOException {
        File file = new File(path);
        if (file == null) {
            FileUtil.createFile(file);
        }
        FileUtil.write(file, true,  JSON.toJSONString(context));
    }
}
