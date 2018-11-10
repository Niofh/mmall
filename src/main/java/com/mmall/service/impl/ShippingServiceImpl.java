package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.ShippingExample;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;


    @Override
    public ServerResponse<PageInfo<Shipping>> getShippingList(Integer pageNum, Integer pageSize,Integer userId) {

        PageHelper.startPage(pageNum,pageSize);

        ShippingExample shippingExample = new ShippingExample();
        ShippingExample.Criteria criteria = shippingExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Shipping> shippingList = shippingMapper.selectByExample(shippingExample);

        PageInfo<Shipping> shippingPageInfo = new PageInfo<>(shippingList);

        return ServerResponse.createBySuccess(shippingPageInfo);
    }

    @Override
    public ServerResponse<Object> addShipping(Integer userId, Shipping shipping) {

        shipping.setUserId(userId);

        int i = shippingMapper.insertSelective(shipping);

        if (i == 0) {
            return ServerResponse.createByErrorMessage("新建地址失败");
        }

        return ServerResponse.createBySuccess("新建地址成功", shipping.getId());
    }

    @Override
    public ServerResponse delShipping(Integer userId, Integer shippingId) {

        int i = shippingMapper.deleteShippingByUserId(userId, shippingId);
        if (i > 0) {
            return ServerResponse.createBySuccess("删除地址成功");
        }

        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    @Override
    public ServerResponse updateShipping(Integer userId, Shipping shipping) {

        shipping.setUserId(userId); // 防止横线越权 shipping userId可能客户端传进来

        int i = shippingMapper.updateByPrimaryKeySelective(shipping);

        if (i > 0) {
            return ServerResponse.createBySuccess("更新地址成功");
        }

        return ServerResponse.createByErrorMessage("更新地址失败");

    }

    @Override
    public ServerResponse<Shipping> selectShipping(Integer userId, Integer shippingId) {

        ShippingExample shippingExample = new ShippingExample();
        ShippingExample.Criteria criteria = shippingExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andIdEqualTo(shippingId);
        List<Shipping> shippings = shippingMapper.selectByExample(shippingExample);

        if(shippings.size()==0){
            return ServerResponse.createByErrorMessage("查询地址失败");
        }

        return ServerResponse.createBySuccess(shippings.get(0));
    }
}
