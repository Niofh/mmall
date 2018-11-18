package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.*;
import com.mmall.pojo.*;
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
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {

    @Value("${file_server_addr}")
    private String imageHost;
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse<OrderProductVo> getOrderCartProduct(Integer userId) {

        List<Cart> cartList = cartMapper.selectCheckCartProduct(userId);

        ServerResponse serverResponse = this.getOrderItem(userId, cartList);

        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }

        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        List<OrderItemVo> orderItemVoList = new ArrayList<>();
        OrderProductVo orderProductVo = new OrderProductVo();
        BigDecimal total = new BigDecimal("0");

        for (OrderItem orderItem : orderItemList) {
            total = BigDecimalUtil.add(orderItem.getTotalPrice().doubleValue(), total.doubleValue());
            OrderItemVo orderItemVo = this.assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }

        orderProductVo.setProductTotalPrice(total);
        orderProductVo.setImageHost(imageHost + "/");
        orderProductVo.setOrderItemVoList(orderItemVoList);

        return ServerResponse.createBySuccess(orderProductVo);
    }


    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        List<Cart> cartList = cartMapper.selectCheckCartProduct(userId);

        ServerResponse serverResponse = this.getOrderItem(userId, cartList);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }

        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        Order order = this.assembleOrder(userId, shippingId, orderItemList);
        if (order == null) {
            return ServerResponse.createByErrorMessage("生成订单失败");
        }
        if (CollectionUtils.isEmpty(orderItemList)) {
            return ServerResponse.createByErrorMessage("购物车为空");
        }

        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo()); // 订单里的商品插入订单编号
            orderItem.setUpdateTime(null);
        }
        // 批量插入订单描述插入数据库
        orderItemMapper.insertOrderItemList(orderItemList);


        // 减少库存
        for (OrderItem orderItem : orderItemList) {
            ProductWithBLOBs product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            Integer stock = product.getStock();
            Integer quantity = orderItem.getQuantity();
            product.setStock(stock - quantity);
            product.setUpdateTime(null);
            // 更新一下库存
            productMapper.updateByPrimaryKeySelective(product);
        }

        // 清空用户购物车已勾选商品
        cartMapper.deleteByUserIdAndChecked(userId);

        OrderVo orderVo = this.assembleOrderVo(order, orderItemList);

        return ServerResponse.createBySuccess(orderVo);
    }


    @Override
    public ServerResponse<PageInfo> getOrderList(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        OrderExample orderExample = new OrderExample();
        OrderExample.Criteria criteria = orderExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<Order> orderList = orderMapper.selectByExample(orderExample); // 获取所有订单

        List<OrderVo> orderVoList = this.assembleOrderVoList(orderList, userId);

        PageInfo pageInfo = new PageInfo(orderVoList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByOrderNoAndUserId(orderNo, userId);
        if (order == null) {
            return ServerResponse.createByErrorMessage("该订单不存在");
        }
        List<OrderItem> orderItemList = orderItemMapper.selectOrderItemListByOrderNo(orderNo, userId);
        OrderVo orderVo = this.assembleOrderVo(order, orderItemList);

        return ServerResponse.createBySuccess(orderVo);
    }

    @Override
    public ServerResponse cancelOrder(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByOrderNoAndUserId(orderNo, userId);
        if (order == null) {
            return ServerResponse.createByErrorMessage("该订单不存在");
        }
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.createByErrorMessage("该订单已付款，不能取消订单");
        }
        Order order1 = new Order();
        order1.setId(order.getId());
        order1.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
//        order1.setCloseTime(new Date());
        int i = orderMapper.updateByPrimaryKeySelective(order1);
        if (i > 0) {
            return ServerResponse.createBySuccess("订单取消成功");
        }

        return ServerResponse.createByError("订单取消失败");
    }


    @Override
    public ServerResponse<PageInfo> getOrderListNoUserIdManager( Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        OrderExample orderExample = new OrderExample();
        List<Order> orderList = orderMapper.selectByExample(orderExample);
        List<OrderVo> orderVoList = this.assembleOrderVoList(orderList, null);
        PageInfo<OrderVo> pageInfo = new PageInfo<>(orderVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<PageInfo> searchOrderManager(Long orderNo, Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        OrderExample orderExample = new OrderExample();
        OrderExample.Criteria criteria = orderExample.createCriteria();
        criteria.andOrderNoEqualTo(orderNo);
        List<Order> orderList = orderMapper.selectByExample(orderExample);
        List<OrderVo> orderVoList = this.assembleOrderVoList(orderList, null);
        PageInfo<OrderVo> pageInfo = new PageInfo<>(orderVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse<OrderVo> getOrderDetailManager(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);

        if (order == null) {
            return ServerResponse.createByErrorMessage("没有找到订单");
        }

        List<OrderItem> orderItemList = orderItemMapper.selectOrderItemListByOrderNo(orderNo, null);

        OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
        return ServerResponse.createBySuccess(orderVo);
    }

    @Override
    public ServerResponse orderSendManager(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);

        if (order == null) {
            return ServerResponse.createByErrorMessage("没有找到订单");
        }

        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
        updateOrder.setSendTime(new Date());

        int i = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (i > 0) {
            ServerResponse.createBySuccess("发货成功");
        }

        return ServerResponse.createByError("发货失败");
    }

    private List<OrderVo> assembleOrderVoList(List<Order> orderList, Integer userId) {
        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order : orderList) {

            List<OrderItem> orderItemList = new ArrayList<>();

            if (userId == null) {
                //todo 管理员查询的时候 不需要传userId
                orderItemList = orderItemMapper.selectOrderItemListByOrderNo(order.getOrderNo(), null);
            } else {
                //todo 防止用户越权查询
                orderItemList = orderItemMapper.selectOrderItemListByOrderNo(order.getOrderNo(), userId);
            }
            OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
            orderVoList.add(orderVo); // 将数据进行过滤返回出去
        }
        return orderVoList;
    }

    /**
     * 组装返回数据
     *
     * @param order
     * @return
     */
    private OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList) {

        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());

        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());

        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if (shipping != null) {
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShipping(assembleShipping(order.getShippingId()));
        }

        orderVo.setPaymentTime(order.getPaymentTime());
        orderVo.setSendTime(order.getSendTime());
        orderVo.setEndTime(order.getEndTime());
        orderVo.setCreateTime(order.getCreateTime());
        orderVo.setCloseTime(order.getCloseTime());

        orderVo.setImageHost(imageHost + "/");

        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    private Shipping assembleShipping(Integer shippingId) {

        return shippingMapper.selectByPrimaryKey(shippingId);

    }

    private OrderItemVo assembleOrderItemVo(OrderItem orderItem) {
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setCreateTime(orderItem.getCreateTime());
        return orderItemVo;
    }


    /**
     * 生成订单
     *
     * @param userId
     * @param shippingId
     * @param orderItemList
     * @return
     */
    private Order assembleOrder(Integer userId, Integer shippingId, List<OrderItem> orderItemList) {
        Order order = new Order();

        order.setOrderNo(this.createOrderNo());
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setPayment(this.getOrderItemTotalPayment(orderItemList));
        order.setPaymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPostage(0);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());

        //发货时间等等
        //付款时间等等
        // 订单插入一条数据
        int i = orderMapper.insertSelective(order);
        if (i > 0) {
            return order;
        }
        return null;
    }


    /**
     * 获取生成订单之前商品总预付价格
     */
    private BigDecimal getOrderItemTotalPayment(List<OrderItem> orderItemList) {
        BigDecimal totalPayment = new BigDecimal("0"); //todo 小心单引号和双引号，作用是不一样的
        for (OrderItem orderItem : orderItemList) {
            totalPayment = BigDecimalUtil.add(totalPayment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }

        return totalPayment;
    }


    /**
     * 创建订单编号(这是没有高并发的时候编号)
     *
     * @return
     */
    private Long createOrderNo() {
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis + new Random().nextInt(100);
    }


    /**
     * 获取封装好的订单明细， 已经判断了是否有库存、是否购物车存在商品、商品是否在售
     *
     * @param userId
     * @return
     */
    private ServerResponse getOrderItem(Integer userId, List<Cart> cartList) {


        if (CollectionUtils.isEmpty(cartList)) {
            return ServerResponse.createByErrorMessage("购物车是空的！");
        }
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Cart cart : cartList) {
            // 判断商品状态
            ProductWithBLOBs product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "不是在线售卖状态");
            }

            if (product.getStock() < cart.getQuantity()) {
                return ServerResponse.createByErrorMessage("商品" + product.getName() + "库存不足");
            }

            OrderItem orderItem = new OrderItem();

            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cart.getQuantity()));

            orderItemList.add(orderItem);
        }

        return ServerResponse.createBySuccess(orderItemList);

    }
}
