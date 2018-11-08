package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

public interface ICartService {

    /**
     * 通过用户id获取购物车产品数量
     *
     * @param userId
     * @return
     */
    ServerResponse<Integer> getCartCountByUserId(Integer userId);


    /**
     * 通过用户id获取购物车列表, 并计算库存，总价格
     * @param userId
     * @return
     */
    ServerResponse<CartVo> getCartList(Integer userId);

    /**
     * 添加商品购物车
     * @param userId
     * @param productId 产品id
     * @param count 产品数量
     * @return
     */
    ServerResponse<CartVo> addCart(Integer userId, Integer productId, Integer count);
}
