package com.dang.etest.util;

import org.junit.Test;

/**
 * Description:
 *
 * @Author dangqihe dangqihe@baidu.com
 * @Date Create in 2018/4/24
 */
public class JavaDocReaderTest {
    @Test
    public void newInstance() throws Exception {
        String paht = System.getProperty("user.dir") + "/src/test/java/com/dang/etest/sample/UserService.java";
        JavaDocReader javaDocReader = JavaDocReader.newInstance(paht);
        //        javaDocReader.show();
    }

}