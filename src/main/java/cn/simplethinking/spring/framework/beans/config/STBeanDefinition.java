package cn.simplethinking.spring.framework.beans.config;

import lombok.Data;

/**
 * @author
 * @description
 * @see
 * @since
 */
@Data
public class STBeanDefinition {

    private String beanClassName;

    private boolean lazyInit = false;

    private String factoryBeanName;
}
