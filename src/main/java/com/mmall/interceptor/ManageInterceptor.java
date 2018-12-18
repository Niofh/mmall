package com.mmall.interceptor;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.common.jedis.RedisShardedPoolUtil;
import com.mmall.pojo.User;
import com.mmall.utli.CookieUtil;
import com.mmall.utli.InterceptorRespoenUtli;
import com.mmall.utli.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理系统全局拦截器
 */
public class ManageInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {


        // 判断cookies 是否存在token
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);

        // 不存在未登录
        if (StringUtils.isEmpty(loginToken)) {
            ServerResponse<String> serverResponse = ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");

            InterceptorRespoenUtli.sendJson(httpServletResponse, serverResponse);

            return false;
        }


        // 排除拦截可以在这里写，不用写在springWeb.xml排除拦截
        HandlerMethod handlerMethod = (HandlerMethod) o;
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName(); // 包含包名

        if (StringUtils.equals("UserController", className) && StringUtils.equals("login", methodName)) {
            // 登录放开拦截
            return true;
        }

        String data = RedisShardedPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(data, User.class);
        // 用户登录信息过期
        if (user == null) {

            if (StringUtils.equals("ProductManageController", className) && StringUtils.equals("richtextImg_Upload", methodName)) {
                // 富文本处理
                Map map = new HashMap<String, String>();

                map.put("success", false);
                map.put("msg", "请登录，管理员");

                InterceptorRespoenUtli.sendJson(httpServletResponse, map);

            } else {

                ServerResponse<String> serverResponse = ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户登录信息已过期");

                InterceptorRespoenUtli.sendJson(httpServletResponse, serverResponse);
            }


            return false;
        }


        Integer role = user.getRole();
        // 角色不是管理员也拒绝访问
        if (role != Const.Roles.ROLE_ADMIN) {

            if (StringUtils.equals("ProductManageController", className) && StringUtils.equals("richtextImg_Upload", methodName)) {
                // 富文本处理
                Map map = new HashMap<String, String>();

                map.put("success", false);
                map.put("msg", "你不是管理员，无权限操作");

                InterceptorRespoenUtli.sendJson(httpServletResponse, map);

            } else {
                ServerResponse<String> serverResponse = ServerResponse.createByErrorMessage("该用户不是管理员,无法有访问权限");
                InterceptorRespoenUtli.sendJson(httpServletResponse, serverResponse);
            }


            return false;
        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
