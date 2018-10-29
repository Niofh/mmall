package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/10/28 22:19
 */
@Controller
public class UserController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping("/login")
    @ResponseBody
    public ServerResponse<User> login(User user) {
        return iUserService.login(user);
    }

}
