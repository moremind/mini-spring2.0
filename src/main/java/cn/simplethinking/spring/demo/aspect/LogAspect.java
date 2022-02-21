package cn.simplethinking.spring.demo.aspect;

import cn.simplethinking.spring.framework.aop.aspect.STJoinPoint;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class LogAspect {

    public void before(STJoinPoint joinPoint) {
        //joinPoint.get
        // 往对象中记录调用的开始时间
    }

    public void after() {
        // 系统当前时间-之前的记录的开始时间=方法的调用锁消耗的时间
        // 检测方法的执行性能
    }

    public void afterThrowing() {
        // 检测异常
    }
}
