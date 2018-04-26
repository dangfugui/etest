package com.dang.etest.core;

import com.dang.etest.model.MarkdownCreater;
import com.dang.etest.model.MethodCaseReader;

/**
 * Description: etest config
 *
 * @Author dangqihe
 * @Date Create in 2018/4/23
 */
public class EtestConfig {

    public static String userDir = System.getProperty("user.dir");
    public static String imageDir = "/src/test/java/";
    public static String docDir = "/src/test/java/";
    public static String srcDir = "/src/main/java/";

    static MethodCaseReader methodCaseReader = new MarkdownCreater();   // 默认生成markdown文档

}
