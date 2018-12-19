package com.mmall.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Controller 打印请求地址，和参数
 */
@Slf4j
public class UrlInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        StringBuffer requestURL = httpServletRequest.getRequestURL();

        log.info("requestURL = " + requestURL);


        if (o instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) o;
            String methodName = handlerMethod.getMethod().getName();
            String className = handlerMethod.getBean().getClass().getName(); // 包含包名

            log.info("methodName = " + methodName);
            log.info("className = " + className);
        }



        // 打印参数
        StringBuffer stringBuffer = new StringBuffer();
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        Iterator<Map.Entry<String, String[]>> iterator = parameterMap.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<String, String[]> item = iterator.next();
            String mapKey = item.getKey();
            String mapValue = StringUtils.EMPTY;

            // request 参数返回String[]
            Object obj = item.getValue();
            if (obj instanceof String[]) {
                mapValue = Arrays.toString((String[]) obj);
            }

            stringBuffer.append(mapKey).append("=").append(mapValue);

        }

        log.info("requestParams:", stringBuffer);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
