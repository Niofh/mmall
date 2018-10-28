package com.mmall.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/10/28 22:19
 */
@Controller
public class UserController {
    @RequestMapping("/login")
    @ResponseBody
    public String login() {
        return "denglu";
    }

}
