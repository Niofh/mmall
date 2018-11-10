package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

public interface IShippingService {

    /**
     * 获取收货地址列表 分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo<Shipping>> getShippingList(Integer pageNum, Integer pageSize,Integer userId);


    /**
     * 添加收货地址
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse<Object> addShipping(Integer userId, Shipping shipping);


    ServerResponse delShipping(Integer userId, Integer shippingId);

    ServerResponse updateShipping(Integer id, Shipping shipping);

    ServerResponse<Shipping>  selectShipping(Integer id, Integer shippingId);

}
