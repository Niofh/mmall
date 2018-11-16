package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderProductVo;
import com.mmall.vo.OrderVo;

public interface IOrderService {

    /*获取购物车勾选的商品信息 （结算）*/
    ServerResponse<OrderProductVo> getOrderCartProduct(Integer userId);

    /**
     * 创建订单
     * @param userId  用户id
     * @param shipping 收货地址id
     * @return
     */
    ServerResponse<OrderVo> createOrder(Integer userId, Integer shipping);

}
