package hbasedemo.hbasedemo.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * HBase annotation 工具类
 *
 * @author hejq
 * @date 2019/5/31 14:56
 */
public class AnnotationUtil {

    public AnnotationUtil() {
    }

    /**
     * 通过引用注解字段获取字段名
     *
     * @param clazz class
     * @param annon 注解
     * @return 注解定义字段名
     */
    public static String getFieldNameByFieldAnnotation(Class clazz, Class<? extends Annotation> annon) {
        String fieldName = null;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annon)) {
                fieldName = field.getName();
                break;
            }
        }
        return fieldName;
    }

    /**
     * 通过引用注解获取对应的值
     *
     * @param model 对象
     * @param annon 注解
     * @param <T> 泛型
     * @return 注解写入的值
     */
    public static <T> Object getFieldValueByFieldAnnotation(T model, Class<? extends Annotation> annon) {
        String fieldName = getFieldNameByFieldAnnotation(model.getClass(), annon);
        return getFieldValueByFieldName(model, fieldName);
    }

    /**
     * 通过引用注解获取对应的值
     *
     * @param model 对象
     * @param fieldName 注解名
     * @param <T> 泛型
     * @return 注解写入的值
     */
    public static <T> Object getFieldValueByFieldName(T model, String fieldName) {
        return JackBeanUtil.getProperty(model, fieldName);
    }

    /**
     * 通过引用注解获取对应的值
     *
     * @param clazz class
     * @param annon 注解名
     * @return 注解写入的值
     */
    public static List<Field> getFieldsBySpecAnnotation(Class clazz, Class<? extends Annotation> annon) {
        List<Field> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annon)) {
                list.add(field);
            }
        }
        return list;
    }

    /**
     * 通过引用注解获取对应的值
     *
     * @param clazz class
     * @param annon 注解名
     * @return 注解写入的值
     */
    public static List<String> getFieldsBySpecAnnotationWithString(Class clazz, Class<? extends Annotation> annon) {
        List<Field> fields = getFieldsBySpecAnnotation(clazz, annon);
        List<String> results = new ArrayList<>();
        Iterator var4 = fields.iterator();
        while (var4.hasNext()) {
            Field field = (Field) var4.next();
            results.add(field.getName());
        }
        return results;
    }

}
