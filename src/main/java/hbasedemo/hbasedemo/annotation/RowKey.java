package hbasedemo.hbasedemo.annotation;

import java.lang.annotation.*;

/**
 * 主键
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RowKey {

}
