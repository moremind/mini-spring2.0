package cn.simplethinking.spring.framework.aop.config;

import lombok.Data;

/**
 * @author
 * @description
 * @see
 * @since
 */
@Data
public class STAopConfig {
    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowing;
    private String aspectAfterThrowingName;


}
