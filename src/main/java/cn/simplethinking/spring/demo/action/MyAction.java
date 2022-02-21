package cn.simplethinking.spring.demo.action;

import cn.simplethinking.spring.framework.annotation.STAutowired;
import cn.simplethinking.spring.framework.annotation.STRequestParam;
import cn.simplethinking.spring.framework.webmvc.servlet.STModelAndView;
import cn.simplethinking.spring.demo.service.QueryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class MyAction {

    @STAutowired
    QueryService queryService;

    public STModelAndView query(HttpServletRequest request, HttpServletResponse response,
                                @STRequestParam("name") String name) {

        try {
            String result = queryService.query(name);
            return out(response, result);
        } catch (Exception exception) {
            Map<String, Object> model = new HashMap<>();
            model.put("detail", exception.getCause().getMessage());
            model.put("stackTrace", exception.getCause().getMessage());
            return new STModelAndView("500", model);
        }

    }

    private STModelAndView out(HttpServletResponse response, String result) {
        try {
            response.getWriter().write(result);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
