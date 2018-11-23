package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    /**
     * 支付宝当面付SDK&Demo https://docs.open.alipay.com/194/105201/
     * @param httpSession
     * @param orderNo
     * @param request
     * @return
     */
    @RequestMapping("/pay")
    @ResponseBody
    public ServerResponse pay(HttpSession httpSession, Long orderNo, HttpServletRequest request) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
          return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String path =request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(user.getId(),orderNo,path);
    }

    /*
    * 支付宝回调地址
    * 接受支付宝所有异步回调的参数,用对象存起来
    * 异步通知验签 ：https://docs.open.alipay.com/200/106120#s1 自行实现验签
    * https://docs.open.alipay.com/194/103296/
    * 验证异步回调的方法
    * */
    @RequestMapping("/alipay_callback")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {
        Map<String, String[]> requestParams = request.getParameterMap();

        if(requestParams.size()==0){
            return ServerResponse.createByError();
        }

        Map<String, String> params = new HashMap<String, String>();

        // 所有key循坏
        for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
            String name = (String)iter.next();
            String[] values = (String[]) requestParams.get(name); // 获取所有值
            String valueStr = "";
            for(int i = 0 ; i <values.length;i++){
                valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
            }
            params.put(name,valueStr);
        }

        params.remove("sign_type");

        try {
            boolean rsaCheckV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if(!rsaCheckV2){
                return ServerResponse.createByErrorMessage("非法请求,验证不通过,再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        //  验证各种数据 https://docs.open.alipay.com/194/103296/ 第五步

        ServerResponse serverResponse = iOrderService.aliCallback(params);

        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS; // 支付成功需要返回success，不然无限轮播请求状态
        }
        return Const.AlipayCallback.RESPONSE_FAILED;

    }

    /**
     * 监听订单状态  前端5面监听一次，监听成功返回成功野
     * @param httpSession
     * @param orderNo
     * @return
     */
    @RequestMapping("/query_order_pay_status")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession httpSession, Long orderNo){
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.queryOrderPayStatus(user.getId(),orderNo);
    }





}
