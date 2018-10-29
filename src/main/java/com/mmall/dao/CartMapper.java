package com.mmall.dao;

import com.mmall.pojo.Cart;
import com.mmall.pojo.CartExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    /**
     * countByExample
     */
    long countByExample(CartExample example);

    /**
     * deleteByExample
     */
    int deleteByExample(CartExample example);

    /**
     * deleteByPrimaryKey
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * insert
     */
    int insert(Cart record);

    /**
     * insertSelective
     */
    int insertSelective(Cart record);

    /**
     * selectByExample
     */
    List<Cart> selectByExample(CartExample example);

    /**
     * selectByPrimaryKey
     */
    Cart selectByPrimaryKey(Integer id);

    /**
     * updateByExampleSelective
     */
    int updateByExampleSelective(@Param("record") Cart record, @Param("example") CartExample example);

    /**
     * updateByExample
     */
    int updateByExample(@Param("record") Cart record, @Param("example") CartExample example);

    /**
     * updateByPrimaryKeySelective
     */
    int updateByPrimaryKeySelective(Cart record);

    /**
     * updateByPrimaryKey
     */
    int updateByPrimaryKey(Cart record);
}