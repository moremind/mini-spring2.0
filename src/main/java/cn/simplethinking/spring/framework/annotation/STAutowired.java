package cn.simplethinking.spring.framework.annotation;


import java.lang.annotation.*;

/**
 * @author
 * @description
 * @see
 * @since
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface STAutowired {
    String value() default "";
}
