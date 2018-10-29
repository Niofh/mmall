package com.mmall.dao;

import com.mmall.pojo.Order;
import com.mmall.pojo.OrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

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
}