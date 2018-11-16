package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.ProductWithBLOBs;
import com.mmall.service.IOrderService;
import com.mmall.utli.BigDecimalUtil;
import com.mmall.vo.OrderItemVo;
import com.mmall.vo.OrderProductVo;
import com.mmall.vo.OrderVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {

    @Value("${file_server_addr}")
    private String imageHost;
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse<OrderProductVo> getOrderCartProduct(Integer userId) {

        List<Cart> cartList = cartMapper.selectCheckCartProduct(userId);
        ServerResponse serverResponse = this.getOrderCartProduct(userId, cartList);

        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }

        List<OrderItemVo> orderItemVoList = (List<OrderItemVo>) serverResponse.getData();
        OrderProductVo orderProductVo = new OrderProductVo();
        BigDecimal total = new BigDecimal("0");

        for (OrderItemVo orderItemVo : orderItemVoList) {
            total=BigDecimalUtil.add(orderItemVo.getTotoalPrice().doubleValue(),total.doubleValue());
        }

        orderProductVo.setProductTotalPrice(total);
        orderProductVo.setImageHost(imageHost + "/");
        orderProductVo.setOrderItemVoList(orderItemVoList);

        return ServerResponse.createBySuccess(orderProductVo);
    }




    @Override
    public ServerResponse<OrderVo> createOrder(Integer userId, Integer shipping){





        return null;
    }


    /**
     * 获取封装好的订单明细
     *
     * @param userId
     * @param cartList
     * @return
     */
    private ServerResponse getOrderCartProduct(Integer userId, List<Cart> cartList) {

        if (CollectionUtils.isEmpty(cartList)) {
            return ServerResponse.createByErrorMessage("购物车是空的！");
        }
        List<OrderItemVo> orderItemVos = new ArrayList<>();
        for (Cart cart : cartList) {
            // 判断商品状态
            ProductWithBLOBs product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "不是在线售卖状态");
            }

            if (product.getStock() < cart.getQuantity()) {
                return ServerResponse.createByErrorMessage("商品" + product.getName() + "库存不足");
            }

            OrderItemVo orderItemVo = new OrderItemVo();

            orderItemVo.setProductId(product.getId());
            orderItemVo.setProductName(product.getName());
            orderItemVo.setProductImage(product.getMainImage());
            orderItemVo.setQuantity(cart.getQuantity());
            orderItemVo.setCreateTime(cart.getCreateTime());
            orderItemVo.setCurrentUntprice(product.getPrice()); // 当前商品价格
            orderItemVo.setTotoalPrice(BigDecimalUtil.mul(cart.getQuantity().doubleValue(), product.getPrice().doubleValue()));
            orderItemVos.add(orderItemVo);
        }

        return ServerResponse.createBySuccess(orderItemVos);

    }
}
