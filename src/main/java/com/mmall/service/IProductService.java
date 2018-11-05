package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.ProductWithBLOBs;
import com.mmall.vo.ProductVo;
import org.springframework.beans.factory.annotation.Value;

/**
 * 商品接口
 *
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/11/5 11:02
 */
public interface IProductService {
    /**
     * 获取商品列表 分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo>  getProductList(Integer pageNum, Integer pageSize);

    /**
     * 查询获取商品列表 分页
     * @param productName 商品名称
     * @param productId  商品id
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getSearchProductList(String productName, Integer productId, int pageNum, int pageSize);

    /**
     * 新增商品
     * @param productVo
     * @return
     */
    ServerResponse addProduct(ProductVo productVo);

    /**
     * 更新商品
     * @param productVo
     * @return
     */
    ServerResponse updateProduct(ProductVo productVo);


    /**
     * 根据id查看商品
     * @param productId
     * @return
     */
    ServerResponse<ProductVo> getProductById(Integer productId);




}
