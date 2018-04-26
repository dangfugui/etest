package com.dang.etest.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dang.etest.core.EtestConfig;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.RootDoc;

/**
 * Description:
 *
 * @Date Create in 2018/4/24
 */
// 一个简单Doclet,收到 RootDoc对象保存起来供后续使用
// https://blog.csdn.net/10km/article/details/78252586
public class JavaDocReader {
    private static final Logger LOG = LoggerFactory.getLogger(JavaDocReader.class);
    private static String path;
    private static Map<String, RootDoc> rootMap = new HashMap<>();

    // 一个简单Doclet,收到 RootDoc对象保存起来供后续使用
    private RootDoc root;

    private JavaDocReader() {

    }

    public static synchronized JavaDocReader newInstance(Object targetObject) {
        String path = EtestConfig.userDir + EtestConfig.srcDir + targetObject.getClass().getName()
                .replaceAll("\\.", "/") + "" + ".java";
        JavaDocReader.path = path;
        List<String> stringList = new ArrayList<>();
        stringList.add("-doclet");
        stringList.add(JavaDocReader.class.getName());
        Class<?> clazz = targetObject.getClass();
        while (clazz != Object.class) {
            stringList.add(EtestConfig.userDir + EtestConfig.srcDir + clazz.getName().replaceAll("\\.", "/") + ".java");
            clazz = clazz.getSuperclass();
        }
        JavaDocReader instance = new JavaDocReader();
        // com.sun.tools.javadoc.Main.execute(new String[] {"-d", "docs", "-sourcepath", "/home/usr/src", "p1", "p2"});
        String[] array = new String[stringList.size()];
        System.arraycopy(stringList.toArray(), 0, array, 0, array.length);
        com.sun.tools.javadoc.Main.execute(array);
        instance.root = rootMap.get(path);
        if (instance.root == null || instance.root.classes().length == 0) {
            LOG.error("class source not find :" + path);
            return null;
        }
        return instance;
    }

    public static boolean start(RootDoc root) {
        JavaDocReader.rootMap.put(path, root);
        return true;
    }

    public static ParamTag getParamTag(MethodDoc methodDoc, String paramName) {
        if (methodDoc == null) {
            return null;
        }
        for (ParamTag paramTag : methodDoc.paramTags()) {
            if (paramName.equals(paramTag.parameterName())) {
                return paramTag;
            }
        }
        return null;
    }

    public ClassDoc getClassDoc() {
        return root.classes()[0];
    }

    public MethodDoc getMethodDoc(Method method) {
        for (ClassDoc classDoc : root.classes()) {
            for (MethodDoc doc : classDoc.methods()) {
                if (doc.name().equals(method.getName()) && doc.parameters().length == method.getParameters().length) {
                    boolean equals = true;
                    for (int i = 0; i < doc.parameters().length; i++) {
                        if (!method.getParameters()[i].getType().getName().equals(doc.parameters()[i].type()
                                .qualifiedTypeName())) {
                            equals = false;
                            break;
                        }
                    }
                    if (equals) {
                        return doc;
                    }
                }
            }
        }
        return null;
    }

}