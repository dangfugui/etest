package com.dang.etest.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Description: 技能 java 语法糖
 *
 * @Author dangqihe
 * @Date Create in 2018/4/17
 */
public class Skill {

    /**
     * 判断对象链是否为空（a.b.c==null  a为空  则返回true 而不是报空指针）
     *
     * @param supplier
     *
     * @return
     */
    public static boolean isNull(Supplier supplier) {
        try {
            Object data = supplier.get();
            if (data == null) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return true;
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param supplier
     *
     * @return
     */
    public static boolean isEmpty(Supplier supplier) {
        if (isNull(supplier)) {
            return true;
        }
        return isEmpty(supplier.get());
    }

    public static boolean notEmpty(Supplier supplier) {
        return !isEmpty(supplier);
    }

    /**
     * 判断数据是否为空
     *
     * @return 是否为空
     *
     * @data 数据
     */
    public static boolean isEmpty(Object data) {
        if (data == null) {
            return true;
        }
        if (data instanceof String) {   // String
            String str = (String) data;
            if (str.length() == 0) {
                return true;
            }
            return false;
        } else if (data instanceof Collection) {    //List  set
            Collection coll = (Collection) data;
            if (coll.size() == 0) {
                return true;
            }
            return false;
        } else if (data instanceof Map) {   // Map
            Map map = (Map) data;
            if (map.size() == 0) {
                return true;
            }
            return false;
        } else if (data.getClass().isArray()) { // Array
            Object[] array = (Object[]) data;
            if (array.length == 0) {
                return true;
            }
            return false;
        } else {
            return false;   // 不知道data 类型 不为null 即 是非空
        }
    }

    public static <T extends Iterable> void forEach(Supplier<T> supplier, Consumer consumer) {
        if (!isNull(supplier)) {
            T a = supplier.get();
            a.forEach(consumer);
        }
    }

    public static <T> T getOrNull(Supplier<T> supplier) {
        try {
            T data = supplier.get();
            if (data == null) {
                return null;
            } else {
                return data;
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static <T> T tryGet(Supplier<T> supplier, T defaultValue) {
        try {
            T data = supplier.get();
            if (data == null) {
                return null;
            } else {
                return data;
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return defaultValue;
        }
    }

}
