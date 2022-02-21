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
public class STMethodBeforeAdviceInterceptor extends STAbstractAspectAdvice implements STMethodInterceptor {

    private STJoinPoint joinPoint;
    public STMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    private void before(Method method, Object[] args, Object target) throws InvocationTargetException, IllegalAccessException {
        //method.invoke(target);
        super.invokeAdviceMethod(this.joinPoint, null, null);
    }

    @Override
    public Object invoke(STMethodInvocation invocation) throws Throwable {
        this.joinPoint = invocation;
        before(invocation.getMethod(), invocation.getArgument(), invocation.getThis());
        return invocation.proceed();
    }
}
