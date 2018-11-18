package com.mmall.dao;

import com.mmall.pojo.OrderItem;
import com.mmall.pojo.OrderItemExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    /**
     *  countByExample
     */
    long countByExample(OrderItemExample example);

    /**
     *  deleteByExample
     */
    int deleteByExample(OrderItemExample example);

    /**
     *  deleteByPrimaryKey
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  insert
     */
    int insert(OrderItem record);

    /**
     *  insertSelective
     */
    int insertSelective(OrderItem record);

    /**
     *  selectByExample
     */
    List<OrderItem> selectByExample(OrderItemExample example);

    /**
     *  selectByPrimaryKey
     */
    OrderItem selectByPrimaryKey(Integer id);

    /**
     *  updateByExampleSelective
     */
    int updateByExampleSelective(@Param("record") OrderItem record, @Param("example") OrderItemExample example);

    /**
     *  updateByExample
     */
    int updateByExample(@Param("record") OrderItem record, @Param("example") OrderItemExample example);

    /**
     *  updateByPrimaryKeySelective
     */
    int updateByPrimaryKeySelective(OrderItem record);

    /**
     *  updateByPrimaryKey
     */
    int updateByPrimaryKey(OrderItem record);

    // 批量插入订单描述
    int insertOrderItemList(@Param("orderItemList") List<OrderItem> orderItemList);

    // 根据订单编号获取订单详情
    List<OrderItem> selectOrderItemListByOrderNo( @Param("orderNo")Long orderNo, @Param("userId")Integer userId);
}