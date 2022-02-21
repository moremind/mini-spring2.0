package cn.simplethinking.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @author
 * @description
 * @see
 * @since
 */
public interface STJoinPoint {
    Object getThis();

    Object[] getArgument();

    Method getMethod();
}
