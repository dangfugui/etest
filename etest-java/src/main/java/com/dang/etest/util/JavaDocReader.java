package com.dang.etest.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;


/**
 * Description:
 *
 * @Date Create in 2018/4/24
 */
// 一个简单Doclet,收到 RootDoc对象保存起来供后续使用
// https://blog.csdn.net/10km/article/details/78252586
public class JavaDocReader {
    private static String path;
    private static Map<String,RootDoc> rootMap = new HashMap<>();

    // 一个简单Doclet,收到 RootDoc对象保存起来供后续使用
    private RootDoc root;

    private JavaDocReader(){

    }

    public static synchronized JavaDocReader newInstance(String path){
        JavaDocReader.path = path;
        JavaDocReader instance = new JavaDocReader();
        // com.sun.tools.javadoc.Main.execute(new String[] {"-d", "docs", "-sourcepath", "/home/usr/src", "p1", "p2"});
        com.sun.tools.javadoc.Main.execute(new String[] {"-doclet",
                JavaDocReader.class.getName(), path});
        instance.root = rootMap.get(path);
        return instance;
    }

    public static boolean start(RootDoc root) {
        JavaDocReader.rootMap.put(path,root);
        return true;
    }

    public RootDoc getRoot() {
        return root;
    }

    public ClassDoc getClassDoc(){
        return root.classes()[0];
    }

    public MethodDoc getMethodDoc(Method method) {
        for(MethodDoc doc : getClassDoc().methods()){
            if(doc.name() == method.getName() && doc.parameters().length == method.getParameters().length){

            }
        }
        return null;
    }

    //    // 显示DocRoot中的基本信息
//    public void show(){
//        ClassDoc[] classes = root.classes();
//        for (int i = 0; i < classes.length; ++i) {
//            System.out.println(classes[i]);
//            System.out.println(classes[i].commentText());
//            for(MethodDoc method:classes[i].methods()){
//                System.out.printf("\t%s\n", method.commentText());
//            }
//        }
//    }
}