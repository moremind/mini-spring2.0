package cn.simplethinking.spring.framework.aop;

import cn.simplethinking.spring.framework.aop.support.STAdvisedSupport;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class STCglibAopProxy implements STAopProxy{
    public STCglibAopProxy(STAdvisedSupport config) {
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
