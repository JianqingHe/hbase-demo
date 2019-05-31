package hbasedemo.hbasedemo.annotation;

import java.lang.annotation.*;

/**
 * 字段名
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    String family() default "";

    String qualifier() default "";
}