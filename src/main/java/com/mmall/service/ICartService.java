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

    /**
     * 更新购物车产品
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    ServerResponse<CartVo> updateCart(Integer userId, Integer productId, Integer count);

    /**
     * 删除多个产品
     * @param userId
     * @param productIds 产品编号 1,2,3
     * @return
     */
    ServerResponse<CartVo> delCart(Integer userId,String productIds);

    /**
     * 全选和反选
     * @param userId
     * @param productId
     * @param checked 0是不选 1是勾选
     * @return
     */
    ServerResponse<CartVo> selectCheckorunCheck(Integer userId,Integer productId,Integer checked);
}
