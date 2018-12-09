package com.mmall.utli;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {
    private final static String COOKIE_DOMAI = ".aa.com";
    private final static String COOKIE_NAME = "mmall_login_token";


    /**
     * 读取cookies
     *
     * @param request
     * @return
     */
    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            log.info("read cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
            if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                log.info("read cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                return cookie.getValue();
            }
        }
        return null;
    }


    /**
     * 写入cookies
     *
     * @param response
     * @param token
     */
    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);

        cookie.setDomain(COOKIE_DOMAI);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        cookie.setMaxAge(60 * 60 * 24 * 365); // 存储时间为一年
        log.info("read cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());

        response.addCookie(cookie);
    }


    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    cookie.setDomain(COOKIE_DOMAI);
                    cookie.setPath("/");
                    cookie.setMaxAge(0); // 设置过期时间为0，代表删除此cookies
                    log.info("read cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }

            }
        }
    }
}
