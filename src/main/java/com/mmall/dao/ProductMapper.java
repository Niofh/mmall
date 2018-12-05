package com.mmall.dao;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.ProductExample;
import com.mmall.pojo.ProductWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    /**
     *  countByExample
     */
    long countByExample(ProductExample example);

    /**
     *  deleteByExample
     */
    int deleteByExample(ProductExample example);

    /**
     *  deleteByPrimaryKey
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  insert
     */
    int insert(ProductWithBLOBs record);

    /**
     *  insertSelective
     */
    int insertSelective(ProductWithBLOBs record);

    /**
     *  selectByExampleWithBLOBs
     */
    List<ProductWithBLOBs> selectByExampleWithBLOBs(ProductExample example);

    /**
     *  selectByExample
     */
    List<Product> selectByExample(ProductExample example);

    /**
     *  selectByPrimaryKey
     */
    ProductWithBLOBs selectByPrimaryKey(Integer id);

    /**
     *  updateByExampleSelective
     */
    int updateByExampleSelective(@Param("record") ProductWithBLOBs record, @Param("example") ProductExample example);

    /**
     *  updateByExampleWithBLOBs
     */
    int updateByExampleWithBLOBs(@Param("record") ProductWithBLOBs record, @Param("example") ProductExample example);

    /**
     *  updateByExample
     */
    int updateByExample(@Param("record") Product record, @Param("example") ProductExample example);

    /**
     *  updateByPrimaryKeySelective
     */
    int updateByPrimaryKeySelective(ProductWithBLOBs record);

    /**
     *  updateByPrimaryKeyWithBLOBs
     */
    int updateByPrimaryKeyWithBLOBs(ProductWithBLOBs record);

    /**
     *  updateByPrimaryKey
     */
    int updateByPrimaryKey(Product record);

    ServerResponse updateProductStatus(@Param("productId") Integer productId, @Param("status")Integer status);
}