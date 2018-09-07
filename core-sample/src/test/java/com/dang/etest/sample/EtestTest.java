package com.dang.etest.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.dang.etest.entity.MethodContext;

/**
 * Description:
 *
 * @Date Create in 2018/6/14
 */
public class EtestTest {

    @Test
    public void testString() throws NoSuchMethodException {
        String res = "hello";
        toJson2Object(res);
    }

    @Test
    public void testStringArray() throws NoSuchMethodException {
        String[] res = new String[] {"1", "2", "3"};
        toJson2Object(res);
    }

    @Test
    public void testStringList() throws NoSuchMethodException {
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        toJson2Object(list);
    }

    @Test
    public void testInt() throws NoSuchMethodException {
        int res = 1234;
        toJson2Object(res);
    }

    @Test
    public void testLong() throws NoSuchMethodException {
        long res = 1234L;
        toJson2Object(res);
    }

    @Test
    public void testDouble() throws NoSuchMethodException {
        double res = 1234.1234;
        toJson2Object(res);
    }

    @Test
    public void testDoubleArray() throws NoSuchMethodException {
        double[] res = new double[] {1.1, 2.2, 3.3};
        toJson2Object(res);
    }

    @Test
    public void testByte() throws NoSuchMethodException {
        byte res = 1 << 6;
        toJson2Object(res);
    }

    @Test
    public void testBool() throws NoSuchMethodException {
        boolean res = true;
        toJson2Object(res);
    }

    @Test
    public void testBean() {
        Student res = new Student();
        toJson2Object(res);
    }

    @Test
    public void testBeanList() {
        List<Student> res = new ArrayList<Student>();
        res.add(new Student());
        Student student = new Student();
        List<Student> friend = new ArrayList<Student>();
        friend.add(new Student());
        student.friend = friend;
        res.add(student);
        toJson2Object(res);
    }

    @Test
    public void testBeanMap() {
        Map<Student, Student> res = new HashMap<Student, Student>();
        res.put(new Student(), new Student());
        Student student = new Student();
        List<Student> friend = new ArrayList<Student>();
        friend.add(new Student());
        student.friend = friend;
        res.put(null, student);
        res = (Map<Student, Student>) toJson2Object(res);
        System.out.println(res);
    }


    private Object toJson2Object(Object object) {
        MethodContext methodContext = new MethodContext();
        methodContext.setResult(object);
        String contextJson = JSON.toJSONString(methodContext);
        MethodContext dbMethodContent = JSON.parseObject(contextJson, MethodContext.class);
        Object result = dbMethodContent.doReturn(object.getClass());
        //        Assert.assertTrue(object.equals(result));
        System.out.println(String.format("%-30s|%-30s|%-30s", result.getClass(), result, JSON.toJSONString(result)));
        return result;
    }

    static class Student {
        public int age = 1;
        public double score = 99.99;
        public String name = "dang";
        public List<Student> friend;
    }
}
