package com.mmall.interceptor;


import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.utli.CookieUtil;
import com.mmall.utli.JsonUtil;
import com.mmall.common.jedis.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 每访问一次就重置seesion时间，防止过期重新登录
 */
public class SessionExpireFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String loginToken = CookieUtil.readLoginToken(request);

        if (StringUtils.isNotEmpty(loginToken)) {
            String userData = RedisPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userData, User.class);
            if (user != null) {
                // 重置redis用户信息时间
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }

        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
