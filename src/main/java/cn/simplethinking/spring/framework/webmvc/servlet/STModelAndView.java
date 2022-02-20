package cn.simplethinking.spring.framework.webmvc.servlet;

import java.util.Map;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class STModelAndView {

    private String viewName;

    private Map<String, ?> model;

    public STModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public STModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
