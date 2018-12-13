package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.utli.CookieUtil;
import com.mmall.utli.JsonUtil;
import com.mmall.common.jedis.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(User user, HttpSession httpSession, HttpServletResponse response) {
        ServerResponse<User> userServerResponse = iUserService.login(user);
        if (!userServerResponse.isSuccess()) {
            return userServerResponse;
        }
        Integer role = userServerResponse.getData().getRole();

        if (role.equals(Const.Roles.ROLE_ADMIN)) { //说明登录的是管理员


            // 多个系统登录成功，就要把seesid加入到cookies和redis当中
            String token = httpSession.getId();
            CookieUtil.writeLoginToken(response, token);
            RedisPoolUtil.setEx(token, JsonUtil.obj2String(user), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            return userServerResponse;
        }
        return ServerResponse.createByErrorMessage("该用户不是管理员,无法有访问权限");
    }
}
