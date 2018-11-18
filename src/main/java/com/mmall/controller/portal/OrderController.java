package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService iOrderService;

    /**
     * 获取订单的商品信息 在购物车已经勾选商品信息（结算）
     * @param httpSession
     * @return
     */
    @RequestMapping("/get_order_cart_product")
    @ResponseBody
    public ServerResponse get_order_cart_product(HttpSession httpSession) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
          return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        return iOrderService.getOrderCartProduct(user.getId());
    }

    @RequestMapping("/create")
    @ResponseBody
    public ServerResponse<OrderVo> createOrder(HttpSession httpSession, Integer shippingId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
          return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.createOrder(user.getId(),shippingId);
    }

    @RequestMapping("/list")
    @ResponseBody
    public ServerResponse<PageInfo> getOrderList(HttpSession httpSession, Integer pageNum, Integer pageSize) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
          return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(),pageNum,pageSize);
    }

    @RequestMapping("/detail")
    @ResponseBody
    public ServerResponse<OrderVo> getOrderDetail(HttpSession httpSession, Long orderNo) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
          return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderDetail(user.getId(),orderNo);
    }


    @RequestMapping("/cancel")
    @ResponseBody
    public ServerResponse cancelOrder(HttpSession httpSession, Long orderNo) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
          return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancelOrder(user.getId(),orderNo);
    }



}
