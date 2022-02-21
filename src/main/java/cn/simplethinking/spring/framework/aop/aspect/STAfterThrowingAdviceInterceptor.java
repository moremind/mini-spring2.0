package cn.simplethinking.spring.framework.aop.aspect;

import cn.simplethinking.spring.framework.aop.intercept.STMethodInterceptor;
import cn.simplethinking.spring.framework.aop.intercept.STMethodInvocation;

import java.lang.reflect.Method;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class STAfterThrowingAdviceInterceptor extends STAbstractAspectAdvice implements STMethodInterceptor {

    private String throwingName;

    public STAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(STMethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Exception e) {
            invokeAdviceMethod(mi, null, e.getCause());
            throw e;
        }
    }

    public void setThrowName(String throwName) {
        this.throwingName = throwName;
    }
}
