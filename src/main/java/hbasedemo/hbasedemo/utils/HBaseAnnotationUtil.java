package hbasedemo.hbasedemo.utils;

import hbasedemo.hbasedemo.annotation.HBaseTable;
import hbasedemo.hbasedemo.annotation.RowKey;

/**
 * HBase annotation 工具类
 *
 * @author hejq
 * @date 2019/5/31 15:41
 */
public class HBaseAnnotationUtil {

    /**
     * 获取表名
     */
    public static String getTableName(Class<?> clazz) {
        return clazz.getAnnotation(HBaseTable.class).tableName();
    }

    /**
     * 获取列簇名
     */
    public static String getFamilyName(Class<?> clazz) {
        return clazz.getAnnotation(HBaseTable.class).family();
    }

    /**
     * 获得model的RowKey字段名
     */
    public static String getRowKeyName(Class clazz) {
        return AnnotationUtil.getFieldNameByFieldAnnotation(clazz, RowKey.class);
    }

    /**
     * 获得model的RowKey的值
     */
    public static <T> Object getRowKeyValue(T model) {
        return AnnotationUtil.getFieldValueByFieldAnnotation(model, RowKey.class);
    }

}
