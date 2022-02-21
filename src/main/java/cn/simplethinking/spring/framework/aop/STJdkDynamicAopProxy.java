package cn.simplethinking.spring.framework.aop;

import cn.simplethinking.spring.framework.aop.intercept.STMethodInvocation;
import cn.simplethinking.spring.framework.aop.support.STAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class STJdkDynamicAopProxy implements STAopProxy, InvocationHandler {

    private STAdvisedSupport advised;

    public STJdkDynamicAopProxy(STAdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.advised.getTargetClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        List<Object> interceptorsAndDynamicMethodMatchers = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, this.advised.getTargetClass());

        STMethodInvocation invocation = new STMethodInvocation(proxy, null, method, args, this.advised.getTargetClass(), null);

        return invocation.proceed();
    }
}
