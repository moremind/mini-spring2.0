package cn.simplethinking.spring.framework.context;

/**
 * @author
 * @description 通过解耦的方式获得IOC容器顶层设计
 * 后面将通过一个监听器去扫描所有的类，只要实现了此接口
 * 将自动调用setApplicationContext()方法
 * @see
 * @since
 */
public interface STApplicationContextAware {
    void setApplicationContext(STApplicationContext applicationContext);
}
