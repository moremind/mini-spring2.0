package cn.simplethinking.spring.test;

import cn.simplethinking.spring.framework.context.STApplicationContext;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class TestCase {
    public static void main(String[] args) {
        STApplicationContext applicationContext = new STApplicationContext("classpath:application.properties");
        System.out.println(applicationContext);
    }
}
