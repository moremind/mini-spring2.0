package cn.simplethinking.spring.framework.beans;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class STBeanWrapper {
    private Object wrapperInstance;
    private Class<?> wrapperClass;

    public STBeanWrapper(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    /**
     * Return the bean instance wrapped by this object.
     */
    public Object getWrappedInstance() {
        return this.wrapperInstance;
    }

    /**
     * Return the type of the wrapped bean instance.
     */
    public Class<?> getWrappedClass() {
        return this.wrapperInstance.getClass();
    }
}
