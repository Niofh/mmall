package com.mmall.dao;

import com.mmall.pojo.Order;
import com.mmall.pojo.OrderExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    /**
     *  countByExample
     */
    long countByExample(OrderExample example);

    /**
     *  deleteByExample
     */
    int deleteByExample(OrderExample example);

    /**
     *  deleteByPrimaryKey
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  insert
     */
    int insert(Order record);

    /**
     *  insertSelective
     */
    int insertSelective(Order record);

    /**
     *  selectByExample
     */
    List<Order> selectByExample(OrderExample example);

    /**
     *  selectByPrimaryKey
     */
    Order selectByPrimaryKey(Integer id);

    /**
     *  updateByExampleSelective
     */
    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderExample example);

    /**
     *  updateByExample
     */
    int updateByExample(@Param("record") Order record, @Param("example") OrderExample example);

    /**
     *  updateByPrimaryKeySelective
     */
    int updateByPrimaryKeySelective(Order record);

    /**
     *  updateByPrimaryKey
     */
    int updateByPrimaryKey(Order record);

    // 根据订单编号和用户获取订单
    Order selectByOrderNoAndUserId(@Param("orderNo") Long orderNo,@Param("userId") Integer userId);

    Order selectByOrderNo(Long orderNo);
}