package cn.simplethinking.spring.framework.context;

import cn.simplethinking.spring.framework.annotation.STAutowired;
import cn.simplethinking.spring.framework.annotation.STController;
import cn.simplethinking.spring.framework.annotation.STService;
import cn.simplethinking.spring.framework.beans.STBeanFactory;
import cn.simplethinking.spring.framework.beans.STBeanWrapper;
import cn.simplethinking.spring.framework.beans.config.STBeanDefinition;
import cn.simplethinking.spring.framework.beans.config.STBeanPostProcessor;
import cn.simplethinking.spring.framework.beans.support.STBeanDefinitionReader;
import cn.simplethinking.spring.framework.beans.support.STDefaultListableBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IOC、DI、MVC、AOP
 *
 * @author      finen
 * @description STApplicationContext
 * @see         STBeanFactory
 * @since       1.0.0
 */
public class STApplicationContext extends STDefaultListableBeanFactory implements STBeanFactory {

    private String[] configLocations;

    private STBeanDefinitionReader reader;

    private boolean isSingleton = true;

    // 作为单例的IOC容器
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    // 通用IOC容器
    private Map<String, STBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    public STApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    public void refresh() {
        // 1.定位，定位配置文件
        reader = new STBeanDefinitionReader(configLocations);

        // 2.加载配置文件，扫描相关的类，把它们封装成BeanDefinition
        List<STBeanDefinition> beanDefinitionList = reader.loadBeanDefinitions();

        // 3.注册，把配置信息放到容器里面(伪IOC容器)
        doRegistryBeanDefinition(beanDefinitionList);

        // 4.把不是延迟加载的类，提前初始化
        doAutowired();
    }

    // 只处理非延时加载的情况
    private void doAutowired() {
        for (Map.Entry<String, STBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private void doRegistryBeanDefinition(List<STBeanDefinition> beanDefinitionList) {
        for (STBeanDefinition beanDefinition : beanDefinitionList) {
            this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    @Override
    public Object getBean(String beanName) throws Exception {

        STBeanDefinition stBeanDefinition = this.beanDefinitionMap.get(beanName);

        // 1.初始化
        Object instance = instantiateBean(beanName, stBeanDefinition);

        // 工厂模式+策略模式，自己创建
        STBeanPostProcessor beanProcessor = new STBeanPostProcessor();

        beanProcessor.postProcessBeforeInitialization(instance, beanName);

        //beanProcessor.postProcessAfterInitialization();

        // 3.把这个对象封装到BeanWrapper
        STBeanWrapper beanWrapper = new STBeanWrapper(instance);



        // 2.拿到BeanWrapper之后，要把BeanWrapper保存到IOC容器中
        //if (this.factoryBeanInstanceCache.containsKey(beanName)) {
        //    throw new Exception("The " + beanName + " is exist!");
        //}
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);

        beanProcessor.postProcessAfterInitialization(instance, beanName);

        // 3.注入
        populateBean(beanName, new STBeanDefinition(), beanWrapper);

        return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
    }

    private void populateBean(String beanName, STBeanDefinition stBeanDefinition, STBeanWrapper stBeanWrapper) {
        Object instance = stBeanWrapper.getWrappedInstance();

        Class<?> clazz = stBeanWrapper.getWrappedClass();
        // 判断只加了注解的类，才执行依赖注入
        if (clazz.isAnnotationPresent(STController.class) || clazz.isAnnotationPresent(STService.class)) {
            return;
        }

        // 获得所有的fields
        Field [] fields = clazz.getFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(STAutowired.class)) {continue;}

            STAutowired autowired = field.getAnnotation(STAutowired.class);

            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }

            // 强制访问
            field.setAccessible(true);

            try {
                if(this.factoryBeanInstanceCache.get(autowiredBeanName) == null){ continue; }
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


    }

    private Object instantiateBean(String beanName, STBeanDefinition stBeanDefinition) {
        // 1.拿到要实例化的对象的类名
        String className = stBeanDefinition.getBeanClassName();

        // 2.反射实例化,得到一个对象
        Object instance = null;
        try {
            // 默认就是单例
            if (this.singletonObjects.containsKey(className)) {
                instance = this.singletonObjects.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.singletonObjects.put(className, instance);
                this.singletonObjects.put(stBeanDefinition.getFactoryBeanName(), instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //// 3.把这个对戏那个封装到BeanWrapper中
        //STBeanWrapper beanWrapper = new STBeanWrapper(instance);

        // 4.把BeanWrapper存到IOC容器中
        return instance;
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }

}
