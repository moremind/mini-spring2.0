package cn.simplethinking.spring.framework.webmvc.servlet;

import cn.simplethinking.spring.framework.annotation.STRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class STHandlerAdapter {

    public boolean supports(Object handler) {
        return  handler instanceof STHandlerMapping;
    }

    public STModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws InvocationTargetException, IllegalAccessException {
        STHandlerMapping handlerMapping = (STHandlerMapping) handler;

        // 把方法的形参列表和request的参数列表所在的顺序一一对应
        Map<String, Integer> paramIndexMapping = new HashMap<>();

        Annotation[][] pa =  handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length; i++) {
            for(Annotation a : pa[i]) {
                if (a instanceof STRequestParam) {
                    // 拿到参数的名称，方便去url去匹配
                    String paramName = ((STRequestParam) a).value();
                    if (!"".equals(paramName.trim())) {
                        paramIndexMapping.put(paramName, i);
                    }
                }
            }
        }

        //获取形参列表
        Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            if (type == HttpServletRequest.class ||
                    type == HttpServletResponse.class) {
                paramIndexMapping.put(type.getName(), i);
            }
        }

        // 获得方法的形参列表
        Map<String, String[]> params =  request.getParameterMap();

        Object [] paramValues = new Object[parameterTypes.length];

        for (Map.Entry<String, String[]> param : params.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]]", "")
                    .replaceAll("\\s", "");
            if (!paramIndexMapping.containsKey(param.getKey())) {
                continue;
            }
            int index = paramIndexMapping.get(param.getKey());
            paramValues[index] = castStringValue(value, parameterTypes[index]);
        }

        if (paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int reqIndex = paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = request;
        }

        if (paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = response;
        }


        Object returnValue = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);
        if (returnValue == null || returnValue instanceof Void) {
            return null;
        }

        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == STModelAndView.class;
        if (isModelAndView) {
            return (STModelAndView) returnValue;
        }
        return null;
    }

    private Object castStringValue(String value, Class<?> parameterType) {
        if (String.class == parameterType) {
            return value;
        }
        if (Integer.class == parameterType) {
            return Integer.valueOf(value);
        }
        else if (Double.class == parameterType) {
            return Double.valueOf(value);
        } else {
            if (value != null) {
                return value;
            }
            return null;
        }
    }
}
