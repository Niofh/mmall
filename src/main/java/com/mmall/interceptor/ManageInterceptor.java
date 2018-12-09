package com.mmall.interceptor;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.utli.CookieUtil;
import com.mmall.utli.InterceptorRespoenUtli;
import com.mmall.utli.JsonUtil;
import com.mmall.utli.RedisPoolUtli;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

        String data = RedisPoolUtli.get(loginToken);

        User user = JsonUtil.string2Obj(data, User.class);
        // 用户登录信息过期
        if (user == null) {
            ServerResponse<String> serverResponse = ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户登录信息已过期");

            InterceptorRespoenUtli.sendJson(httpServletResponse, serverResponse);

            return false;
        }


        Integer role = user.getRole();
        // 角色不是管理员也拒绝访问
        if (role != Const.Roles.ROLE_ADMIN) {

            ServerResponse<String> serverResponse = ServerResponse.createByErrorMessage("该用户不是管理员,无法有访问权限");
            InterceptorRespoenUtli.sendJson(httpServletResponse, serverResponse);

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
