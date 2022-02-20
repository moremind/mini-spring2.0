package cn.simplethinking.spring.framework.beans.support;

import cn.simplethinking.spring.framework.beans.config.STBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class STBeanDefinitionReader {

    private Properties config = new Properties();

    // 固定配置文件中的key
    private final String SCAN_PACKAGE = "scanPackage";

    private List<String> registryBeanClasses = new ArrayList<>();



    public STBeanDefinitionReader(String... locations) {
        // 通过URL定位找到其对应的文件，然后转为文件流
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));
        try {
            config.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScanner(config.getProperty(SCAN_PACKAGE));

    }

    private void doScanner(String scanPackage) {
        //转换为文件路径，实际上就是把.替换为/就OK了
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if(file.isDirectory()){
                doScanner(scanPackage + "." + file.getName());
            }else{
                if(!file.getName().endsWith(".class")){ continue;}
                String className = (scanPackage + "." + file.getName().replace(".class",""));
                registryBeanClasses.add(className);
            }
        }
    }

    public Properties getConfig() {
        return this.config;
    }

    // 把配置文件中扫描的所有配置信息转换为STBeanDefinition对象，以便IOC操作方便
    public List<STBeanDefinition> loadBeanDefinitions(String... locations) {
        List<STBeanDefinition> result = new ArrayList<>();

        try {
            for (String className : registryBeanClasses) {
                Class<?> beanClass = Class.forName(className);

                if (beanClass.isInterface()) {continue;}

                // beanName有三种情况
                // 1.默认类名首字母小写
                // 2.自定义名字
                // 3.接口注入
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));

                Class<?> [] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    result.add(doCreateBeanDefinition(i.getName(), beanClass.getName()));
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return result;

    }

    // 把没一个配置信息解析称一个BeanDefinition
    private STBeanDefinition doCreateBeanDefinition(String factoryBeanName,String beanClassName) {
        STBeanDefinition stBeanDefinition = new STBeanDefinition();
        stBeanDefinition.setBeanClassName(beanClassName);
        stBeanDefinition.setFactoryBeanName(factoryBeanName);
        return stBeanDefinition;
    }

    /**
     * 默认不存在类名首字母小写的情况
     * @param simpleName
     * @return
     */
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        // 之所以加32，是因为大小写字母恶ASCII码相差32
        // 而且大写字母的ASCII码小于小写字母的ASCII码
        // 在Java中，对char作算学运算，实际上就是对ASCII码做运算
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
