package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
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
 * @Date: 2018/10/28 22:19
 */
@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(User user, HttpSession httpSession) {
        ServerResponse<User> userServerResponse = iUserService.login(user);
        if (userServerResponse.isSuccess()) {
            // 登录成功，把用户资料存放在httpSession
            httpSession.setAttribute(Const.CURRENT_USER, userServerResponse.getData());
        }
        return userServerResponse;
    }


    /**
     * 退出登录
     *
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> login(HttpSession httpSession) {
        httpSession.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }


    /**
     * 注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addUser(User user) {
        ServerResponse<String> stringServerResponse = iUserService.addUser(user);
        return stringServerResponse;
    }

    /**
     * 校验参数
     *
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "/check_valid", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    @RequestMapping(value = "/get_user_info", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccessMessage("用户未登录，无法获取用户信息");

        }
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 根据用户名称获取问题
     *
     * @return
     */
    @RequestMapping(value = "/forget_get_question", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> getQuestion(String username) {

        return iUserService.getQuestionByUserName(username);
    }


    /**
     * 验证答案
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "/forget_check_answer", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 忘记密码修改密码
     *
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "/forget_reset_password", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> uodateForgetPassword(String username, String passwordNew, String forgetToken) {

        return iUserService.uodateForgetPassword(username, passwordNew, forgetToken);
    }

    /**
     * 修改密码
     *
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @param httpSession session
     * @return
     */
    @RequestMapping(value = "/reset_password", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> updateResetPassword(String passwordOld, String passwordNew, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccessMessage("用户未登录，无法获取用户信息");
        }
        return iUserService.updateResetPassword(passwordOld, passwordNew, user);
    }

    /**
     * 根据id获取用户信息
     * @param httpSession session
     * @return
     */
    @RequestMapping(value = "/get_information", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformationById(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(),"用户未登录，需要强制登录");
        }
        return iUserService.getInformationById(user.getId());
    }

    /**
     * 更新用户信息
     * @param u
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/update_information", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformationById(User u,HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(),"用户未登录，需要强制登录");
        }
        u.setId(user.getId());
        u.setPassword(null);
        u.setUsername(null);
        return iUserService.updateInformationById(u);
    }

}
