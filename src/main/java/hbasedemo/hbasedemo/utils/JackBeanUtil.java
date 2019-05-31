package hbasedemo.hbasedemo.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * bean 操作工具类
 *
 * @author hejq
 * @date 2019/5/31 14:57
 */
public class JackBeanUtil {

    public JackBeanUtil() {
    }

    /**
     * 获取指定bean的字段信息
     *
     * @param clazz bean
     * @return 字段信息
     */
    public static List<Field> getBeanFieldsByClass(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.asList(fields);
    }

    /**
     * 获取指定字段的值
     *
     * @param bean bean对象
     * @param name 字段名称
     * @return 该字段的值
     */
    public static Object getProperty(Object bean, String name) {
        Object value = null;
        String firstLetter = name.substring(0, 1).toUpperCase();
        String methodName = "get" + firstLetter + name.substring(1);
        Method method;
        try {
            method = bean.getClass().getMethod(methodName);
            value = method.invoke(bean);
        } catch (Exception var7) {
            var7.printStackTrace();
        }
        return value;
    }

    /**
     * 给指定字段赋值
     *
     * @param bean bean对象
     * @param name 字段名称
     * @param value 传入的值
     */
    public static void setProperty(Object bean, String name, Object value) {
        String firstLetter = name.substring(0, 1).toUpperCase();
        String setMethodName = "set" + firstLetter + name.substring(1);
        Method method = null;
        try {
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method1 : methods) {
                if (setMethodName.equals(method1.getName())) {
                    method = method1;
                    break;
                }
            }
            Objects.requireNonNull(method).invoke(bean, value);
        } catch (Exception var11) {
            var11.printStackTrace();
        }
    }

    /**
     * 实例化bean
     *
     * @param clazz bean 对象
     * @param <T> 泛型
     * @return 实例化结果
     */
    public static <T> T getInstanceByClass(Class<T> clazz) {
        T model = null;
        try {
            model = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException var3) {
            var3.printStackTrace();
        }
        return model;
    }
}
