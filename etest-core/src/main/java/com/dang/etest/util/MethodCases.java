package com.dang.etest.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dang.etest.core.EtestConfig;
import com.dang.etest.core.EtestFactory;
import com.dang.etest.entity.MethodCase;

/**
 * Description: 测试方法用例 存储类
 *
 * @Author dangqihe dangqihe@baidu.com
 * @Date Create in 2018/5/10
 */
public class MethodCases {
    private static final Logger LOG = LoggerFactory.getLogger(MethodCases.class);
    private static final Map<String, ReaderMethodMapContent> allReadMap = new HashMap<>();

    public static synchronized Map<Method, MethodCase> readerMethodMap(String useClassName, Object targetObject) {
        String key = useClassName + "_" + Classes.getRealClass(targetObject.getClass()).getSimpleName();
        ReaderMethodMapContent res = allReadMap.get(key);
        if (res == null) {
            res = new ReaderMethodMapContent(useClassName, targetObject);
            allReadMap.put(key, res);
        }
        return res.readerMethodMap;
    }

    private static void saveMethodCases() {
        if (EtestConfig.methodCaseReader == null) {
            return;
        }
        for (Map.Entry<String, ReaderMethodMapContent> entry : allReadMap.entrySet()) {
            try {
                EtestConfig.methodCaseReader.doReader(entry.getValue().useClassName, entry.getValue().readerMethodMap,
                        entry.getValue().targetObject);
                LOG.info("ShutdownHook");
            } catch (Exception e) {
                LOG.error("create doc error", e);
            }
        }

    }

    static class ReaderMethodMapContent {
        String useClassName;
        Object targetObject;
        Map<Method, MethodCase> readerMethodMap;

        public ReaderMethodMapContent(String useClassName, Object targetObject) {
            this.useClassName = useClassName;
            this.targetObject = targetObject;
            this.readerMethodMap = new HashMap<>();
        }
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                saveMethodCases();
            }
        }));
    }

}
