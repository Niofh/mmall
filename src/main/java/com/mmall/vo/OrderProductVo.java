package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

public class OrderProductVo {
    private String imageHost; // 图片前缀
    private BigDecimal productTotalPrice; // 购物车总价格
    private List<OrderItemVo> orderItemVoList; // 订单明细

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public List<OrderItemVo> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVo> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    @Override
    public String toString() {
        return "OrderProductVo{" +
                "imageHost='" + imageHost + '\'' +
                ", productTotalPrice=" + productTotalPrice +
                ", orderItemVoList=" + orderItemVoList +
                '}';
    }
}
