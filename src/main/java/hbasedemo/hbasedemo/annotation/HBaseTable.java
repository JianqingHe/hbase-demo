package hbasedemo.hbasedemo.annotation;

import java.lang.annotation.*;

/**
 * 表信息
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface HBaseTable {

    /**
     * 表名
     *
     * @return
     */
    String tableName();

    /**
     * 列簇
     *
     * @return
     */
    String family();
}
