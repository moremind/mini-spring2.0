package cn.simplethinking.spring.framework.webmvc.servlet;

import java.io.File;
import java.util.Locale;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class STViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;

    private String viewName;

    public STViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
    }

    public STView resolveViewName(String viewName, Locale locale) {
        if (viewName == null || "".equals(viewName.trim())) {
            viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : viewName+DEFAULT_TEMPLATE_SUFFIX ;

            File templateFile =  new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
            new STView(templateFile);
        }

        return null;
    }

    public File getTemplateRootDir() {
        return templateRootDir;
    }

    public String getViewName() {
        return viewName;
    }
}
