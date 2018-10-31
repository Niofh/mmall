package com.mmall.interceptor;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.utli.InterceptorRespoenUtli;
import com.mmall.utli.JacksonUtli;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * 管理系统全局拦截器
 */
public class ManageInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {



        // 判断session用户是否存在
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            ServerResponse<String> serverResponse = ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");

            InterceptorRespoenUtli.sendJson(httpServletResponse,serverResponse);

            return false;
        }


        Integer role = user.getRole();
        // 角色不是管理员也拒绝访问
        if (role != Const.Roles.ROLE_ADMIN) {

            ServerResponse<String> serverResponse = ServerResponse.createByErrorMessage("该用户不是管理员,无法有访问权限");
            InterceptorRespoenUtli.sendJson(httpServletResponse,serverResponse);

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
