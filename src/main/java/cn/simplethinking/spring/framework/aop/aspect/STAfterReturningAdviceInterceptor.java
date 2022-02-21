package cn.simplethinking.spring.framework.aop.aspect;

import cn.simplethinking.spring.framework.aop.intercept.STMethodInterceptor;
import cn.simplethinking.spring.framework.aop.intercept.STMethodInvocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class STAfterReturningAdviceInterceptor extends STAbstractAspectAdvice implements STMethodInterceptor {

    private STJoinPoint joinPoint;

    public STAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(STMethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(retVal, mi.getMethod(), mi.getArgument(), mi.getThis());
        return retVal;
    }

    private void afterReturning(Object retVal, Method method, Object[] argument, Object aThis) throws InvocationTargetException, IllegalAccessException {
        super.invokeAdviceMethod(this.joinPoint, retVal, null);
    }

}
