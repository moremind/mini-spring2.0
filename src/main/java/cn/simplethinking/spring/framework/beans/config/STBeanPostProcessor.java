package cn.simplethinking.spring.framework.beans.config;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class STBeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
