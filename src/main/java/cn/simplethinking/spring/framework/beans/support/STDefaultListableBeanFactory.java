package cn.simplethinking.spring.framework.beans.support;

import cn.simplethinking.spring.framework.beans.config.STBeanDefinition;
import cn.simplethinking.spring.framework.context.support.STAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class STDefaultListableBeanFactory extends STAbstractApplicationContext {

    /** Map of bean definition objects, keyed by bean name */
    //存储注册信息的BeanDefinition
    protected final Map<String, STBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
}
