package cn.simplethinking.spring.framework.aop.intercept;

/**
 * @author
 * @description
 * @see
 * @since
 */
public interface STMethodInterceptor {

    Object invoke(STMethodInvocation invocation) throws Throwable;
}
