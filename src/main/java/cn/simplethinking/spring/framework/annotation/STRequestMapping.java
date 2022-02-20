package cn.simplethinking.spring.framework.annotation;

//import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * @author
 * @description
 * @see
 * @since
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Mapping
public @interface STRequestMapping {
    String value() default "";
}
