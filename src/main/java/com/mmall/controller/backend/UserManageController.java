package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/10/30 17:14
 */
@RequestMapping("/manage/user")
@Controller
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(User user, HttpSession httpSession) {
        ServerResponse<User> userServerResponse = iUserService.login(user);
        if (!userServerResponse.isSuccess()) {
            return userServerResponse;
        }
        Integer role = userServerResponse.getData().getRole();

        if (role.equals(Const.Roles.ROLE_ADMIN)) {
            //说明登录的是管理员
            httpSession.setAttribute(Const.CURRENT_USER, userServerResponse.getData());
            return userServerResponse;
        }
        return ServerResponse.createByErrorMessage("该用户不是管理员,无法有访问权限");
    }
}
