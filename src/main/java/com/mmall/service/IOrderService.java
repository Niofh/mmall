package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderProductVo;
import com.mmall.vo.OrderVo;

import java.util.Map;

public interface IOrderService {

    /*获取购物车勾选的商品信息 （结算）*/
    ServerResponse<OrderProductVo> getOrderCartProduct(Integer userId);

    /**
     * 创建订单
     * @param userId  用户id
     * @param shippingId 收货地址id
     * @return
     */
    ServerResponse<OrderVo> createOrder(Integer userId, Integer shippingId);

    /**
     * 获取订单列表
     * @param userId
     * @return
     */
    ServerResponse<PageInfo> getOrderList(Integer userId, Integer pageNum, Integer pageSize);

    ServerResponse<OrderVo> getOrderDetail(Integer userId,Long orderNo);

    /**
     * 取消订单
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse cancelOrder(Integer userId, Long orderNo);


    /**
     * 订单支付
     * @param userId
     * @param orderNo 订单编号
     * @param path  上传文件的前缀
     * @return
     */
    ServerResponse pay(Integer userId,Long orderNo,String path);


    /**
     * 验证异步支付宝数据并且更新
     * @param params
     * @return
     */
    ServerResponse aliCallback(Map<String, String> params);


    /**
     * 前台监听订单的状态
     * @param id
     * @param orderNo
     * @return
     */
    ServerResponse<Boolean> queryOrderPayStatus(Integer id, Long orderNo);




    /**
     * 管理员获取所有订单数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getOrderListNoUserIdManager(Integer pageNum, Integer pageSize);


    /**
     * 管理员按订单号查询订单
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */

    ServerResponse<PageInfo> searchOrderManager(Long orderNo,Integer pageNum, Integer pageSize);

    ServerResponse<OrderVo> getOrderDetailManager(Long orderNo);


    /**
     * 订单发货功能
     * @param orderNo
     * @return
     */
    ServerResponse orderSendManager(Long orderNo);


}
