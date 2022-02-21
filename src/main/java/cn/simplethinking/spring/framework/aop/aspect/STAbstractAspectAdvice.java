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
public abstract class STAbstractAspectAdvice implements STAdvice {

    private Method aspectMethod;

    private Object aspectTarget;

    public STAbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    public Object invokeAdviceMethod(STJoinPoint jointPoint, Object returnValue, Throwable tx) throws InvocationTargetException, IllegalAccessException {
        Class<?> [] paramTypes = this.aspectMethod.getParameterTypes();
        if (null == paramTypes || paramTypes.length == 0) {
            return this.aspectMethod.invoke(aspectTarget);
        } else {
            Object [] args = new Object[paramTypes.length];

            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i] == STJoinPoint.class) {
                    args[i] = jointPoint;
                } else if (paramTypes[i] == Throwable.class) {
                    args[i] = tx;
                } else if (paramTypes[i] == Object.class) {
                    args[i] = returnValue;
                }
            }
            return this.aspectMethod.invoke(aspectTarget, args);
        }
    }
}
