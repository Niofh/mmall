package com.mmall.utli;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 封装拦截器返回数据类型
 */
public class InterceptorRespoenUtli {

    /**
     * 以流方式发送json数据
     * @param response HttpServletResponse
     * @param o java类型pojo
     */
    public static void sendJson(HttpServletResponse response, Object o) {
        response.setContentType("application/json; charset=utf-8");

        try {
            PrintWriter writer = response.getWriter();
            // json数据写入到浏览器中
            writer.append(JacksonUtli.objectToJson(o));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
