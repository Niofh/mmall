package com.mmall.dao;

import com.mmall.pojo.Cart;
import com.mmall.pojo.CartExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {

    /**
     * 获取用户购物车总数量
     * @param userId
     * @return
     */
    int getUserCartTotal(@Param("userId") Integer userId);

    /**
     * 通过userid查询购物车列表
     * @param userId
     * @return
     */
    List<Cart> selectCartsByUserId (@Param("userId") Integer userId);


    int delCartByuserIdandProIds(@Param("userId") Integer userId, @Param("productIdList")List<String> productIdList);


    // 更新是否勾选
    int updateCheckorUncheck(@Param("userId")Integer userId, @Param("productId")Integer productId, @Param("checked")Integer checked);


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

    /**
     * 获取购物车已经勾选的商品
     * @param userId
     * @return
     */
    List<Cart> selectCheckCartProduct(Integer userId);
}