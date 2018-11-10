package com.mmall.dao;

import com.mmall.pojo.Shipping;
import com.mmall.pojo.ShippingExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {


    int deleteShippingByUserId(@Param("userId") Integer userId, @Param("shippingId")Integer shippingId);


    /**
     *  countByExample
     */
    long countByExample(ShippingExample example);

    /**
     *  deleteByExample
     */
    int deleteByExample(ShippingExample example);

    /**
     *  deleteByPrimaryKey
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  insert
     */
    int insert(Shipping record);

    /**
     *  insertSelective
     */
    int insertSelective(Shipping record);

    /**
     *  selectByExample
     */
    List<Shipping> selectByExample(ShippingExample example);

    /**
     *  selectByPrimaryKey
     */
    Shipping selectByPrimaryKey(Integer id);

    /**
     *  updateByExampleSelective
     */
    int updateByExampleSelective(@Param("record") Shipping record, @Param("example") ShippingExample example);

    /**
     *  updateByExample
     */
    int updateByExample(@Param("record") Shipping record, @Param("example") ShippingExample example);

    /**
     *  updateByPrimaryKeySelective
     */
    int updateByPrimaryKeySelective(Shipping record);

    /**
     *  updateByPrimaryKey
     */
    int updateByPrimaryKey(Shipping record);

}