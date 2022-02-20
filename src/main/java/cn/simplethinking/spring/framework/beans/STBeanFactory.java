package cn.simplethinking.spring.framework.beans;

/**
 * @author
 * @description 单例工厂的顶层设计
 * @see
 * @since
 */
public interface STBeanFactory {

    /**
     * 根据beanName从IOC容器获得一个实例bean
     * @param beanName
     * @return
     */
    Object getBean(String beanName) throws Exception;

}
