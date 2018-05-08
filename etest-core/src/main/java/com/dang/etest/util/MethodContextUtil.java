package com.dang.etest.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dang.etest.core.EtestConfig;
import com.dang.etest.entity.MethodContext;

/**
 * Description:
 *
 * @Author dangqihe dangqihe
 * @Date Create in 2018/4/22
 */
public class MethodContextUtil {

    private static Logger LOG = LoggerFactory.getLogger(MethodContextUtil.class);
    private static Map<String, Map<String, MethodContext>> allContent = new HashMap<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                saveMethodContextMap();
            }
        }));
    }

    /**
     * 读取方法镜像map
     *
     * @return 方法镜像map
     */
    public static Map<String, MethodContext> readMethodContextMap(String useClassName, Object targetObject) {
        String path = EtestConfig.userDir + EtestConfig.imageDir +
                useClassName.replaceAll("\\.", "/") + "." + targetObject.getClass().getSimpleName() + ".image";
        Map<String, MethodContext> res = allContent.get(path);
        if (res == null) {
            res = new LinkedHashMap<>();
            allContent.put(path, res);
        }
        File file = new File(path);
        if (file == null || !file.exists()) {
            return res;
        }
        try {
            List<String> list = FileUtil.readAsList(file);
            for (String line : list) {
                if (Skill.isEmpty(line)) {
                    continue;
                }
                MethodContext context = JSON.parseObject(line, MethodContext.class);
                if (context.getKey() == null) {
                    context.setKey(buildKey(context.getClassName(), context.getMethod(), context.getArgs()));
                }
                res.put(context.getKey(), context);
            }
        } catch (IOException e) {
            LOG.error("file read error", e);
        }
        return res;
    }

    /**
     * 保存方法镜像map
     */
    private static void saveMethodContextMap() {
        if (!EtestConfig.changeImage) {   // 是否要更改镜像文件
            return;
        }
        for (Map.Entry<String, Map<String, MethodContext>> contents : allContent.entrySet()) {
            File file = new File(contents.getKey());
            if (file != null && !file.exists()) {
                try {
                    file = FileUtil.createFile(file);
                } catch (IOException e) {
                    LOG.error("create File error", e);
                    return;
                }
            }
            StringBuffer buffer = new StringBuffer();
            for (Map.Entry<String, MethodContext> entry : contents.getValue().entrySet()) {
                buffer.append(JSON.toJSONString(entry.getValue())).append("\n");
            }
            try {
                FileUtil.write(file, false, buffer.toString());
            } catch (IOException e) {
                LOG.error("write file error", e);
            }
        }
    }

    /**
     * 构建map key
     *
     * @param className  类名
     * @param methodName 方法名
     * @param args       参数
     *
     * @return key
     */
    public static String buildKey(String className, String methodName, Object[] args) {
        StringBuffer key = new StringBuffer();
        key.append(className).append(".");
        key.append(methodName).append("(");
        if (args != null) {
            key.append(MethodContext.argsMd5(args)).append(")");
        }
        return key.toString();
    }

    //    public static MethodContext findMethodContext(String useClassName, Object targetObject) {
    //    }
    //
    //    public static void saveMethodContext(String useClassName, Object targetObject, MethodContext context) {
    //    }
}
