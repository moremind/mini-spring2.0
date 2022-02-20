package cn.simplethinking.spring.framework.webmvc.servlet;

import cn.simplethinking.spring.framework.annotation.STController;
import cn.simplethinking.spring.framework.annotation.STRequestMapping;
import cn.simplethinking.spring.framework.context.STApplicationContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author
 * @description
 * @see
 * @since
 */
@Slf4j
public class STDispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = -8572405321424353703L;

    private final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private STApplicationContext applicationContext;

    private List<STHandlerMapping> handlerMappings = new ArrayList<>();

    private Map<STHandlerMapping, STHandlerAdapter> handlerAdapter = new HashMap<>();

    private List<STViewResolver> viewResolvers;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception, Detail :" + Arrays.toString(e.getStackTrace()));
        }

    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException, InvocationTargetException, IllegalAccessException {
        // 1.通过从request中拿到URL，去匹配一个HandlerMapping
        STHandlerMapping handler = getHandler(req);
        if (handler == null) {
            processDispatchResult(req, resp, new STModelAndView("404"));
            // new ModelAndView("404");
            return;
        }

        // 2.准备调用前的参数
        STHandlerAdapter handlerAdapter = getHandlerAdapter(handler);

        // 3.真正的调用方法,返回ModelAndView存储了要穿页面上的值和模板的名称
        STModelAndView mv = handlerAdapter.handle(req, resp, handler);



        // 这一个才是真正的输出
        processDispatchResult(req, resp, mv);
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, STModelAndView mv) throws IOException {
        // 调用页面转换
        if (null == mv) {
            return;
        }

        // 如果ModelAndView不为null
        if(this.viewResolvers.isEmpty()) {return;}

        for (STViewResolver viewResolver : this.viewResolvers) {
            STView view = viewResolver.resolveViewName(mv.getViewName(), null);
            view.render(mv.getModel(), req, resp);
            return;
        }

    }

    private STHandlerAdapter getHandlerAdapter(STHandlerMapping handler) {
        if (this.handlerAdapter.isEmpty()) {return null;}
        STHandlerAdapter handlerAdapter = this.handlerAdapter.get(handler);
        if (handlerAdapter.supports(handler)) {
            return handlerAdapter;
        }
        return null;
    }


    private STHandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()) {
            return null;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath, "").replaceAll("/+", "/");

        for (STHandlerMapping handler : this.handlerMappings) {
            try {
                Matcher matcher = handler.getPattern().matcher(url);
                if (!matcher.matches()){ continue; }
                return handler;
            } catch (Exception e) {
                throw e;
            }
        }
        return null;

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 1.初始化ApplicationContext
        applicationContext = new STApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));

        // 2.初始化Spring MVC 9大组件
        initStrategies(applicationContext);
    }

    protected void initStrategies(STApplicationContext context) {

        //多文件上传的组件
        initMultipartResolver(context);
        //初始化本地语言环境
        initLocaleResolver(context);
        //初始化模板处理器
        initThemeResolver(context);

        // 必须实现
        //handlerMapping
        initHandlerMappings(context);

        // 必须实现
        //初始化参数适配器
        initHandlerAdapters(context);
        //初始化异常拦截器
        initHandlerExceptionResolvers(context);
        //初始化视图预处理器
        initRequestToViewNameTranslator(context);

        // 必须实现
        //初始化视图转换器
        initViewResolvers(context);
        //参数缓存器
        initFlashMapManager(context);
    }

    private void initFlashMapManager(STApplicationContext context) {
    }

    private void initViewResolvers(STApplicationContext context) {
        // 拿到一个模版存放的目录
        String templateRoot = context.getConfig().getProperty("templateRoot");

        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);

        String[] templates = templateRootDir.list();

        for (int i = 0; i < templates.length; i++) {
            // 这里主要是为了兼容模版，所有模仿Spring用list保存
            // 所有为了仿真，所有还是搞个List
            this.viewResolvers.add(new STViewResolver(templateRoot));
        }
    }

    private void initRequestToViewNameTranslator(STApplicationContext context) {
    }

    private void initHandlerMappings(STApplicationContext context) {

        String [] beanNames = context.getBeanDefinitionNames();

        try {
            for (String beanName : beanNames) {
                Object controller = context.getBean(beanName);

                Class<?> clazz = controller.getClass();

                if (!clazz.isAnnotationPresent(STController.class)) {
                    continue;
                }

                String baseUrl = "";
                if (clazz.isAnnotationPresent(STRequestMapping.class)) {
                    STRequestMapping requestMapping = clazz.getAnnotation(STRequestMapping.class);
                    baseUrl = requestMapping.value();
                }

                // 默认获取所有的public方法
                for (Method method : clazz.getMethods()) {
                    if (!method.isAnnotationPresent(STRequestMapping.class)) {
                        continue;
                    }
                    STRequestMapping requestMapping = method.getAnnotation(STRequestMapping.class);

                    // 优化
                    String regex = ("/" + (baseUrl) + "/" + requestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");

                    Pattern pattern = Pattern.compile(regex);
                    this.handlerMappings.add(new STHandlerMapping(pattern, controller, method));

//                handlerMapping.put(url, method);
                   log.info("Mapped :" + regex + "," + method);
                }

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void initHandlerAdapters(STApplicationContext context) {

        // 把一个request请求变成一个handler，参数都是字符串，自动匹配到handler


        // 要拿到HandlerMapping才能干活
        // 意味着有几个HandlerMapping就有几个HandlerAdapter

        for (STHandlerMapping handlerMapping : this.handlerMappings) {
            this.handlerAdapter.put(handlerMapping, new STHandlerAdapter());
        }

    }

    private void initHandlerExceptionResolvers(STApplicationContext context) {
    }

    private void initThemeResolver(STApplicationContext context) {
    }

    private void initLocaleResolver(STApplicationContext context) {
    }

    private void initMultipartResolver(STApplicationContext context) {
    }
}
