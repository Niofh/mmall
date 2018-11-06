package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.pojo.ProductExample;
import com.mmall.pojo.ProductWithBLOBs;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/11/5 11:09
 */
@Service
public class ProductServiceImpl implements IProductService {


    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Value("${file_server_addr}")
    private String fileAddress;

    @Override
    public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectByExample(new ProductExample());
        PageInfo pageInfo = new PageInfo<Product>(productList);

        return ServerResponse.createBySuccess(pageInfo);
    }


    @Override
    public ServerResponse<PageInfo> getSearchProductList(String productName, Integer productId, int pageNum, int pageSize) {

        // 分页插件调用
        PageHelper.startPage(pageNum, pageSize);
        ProductExample productExample = new ProductExample();
        ProductExample.Criteria criteria = productExample.createCriteria();

        if (productId != null) {
            criteria.andIdEqualTo(productId);
        }

        if (StringUtils.isNoneBlank(productName)) {
            String searchName = "%" + productName + "%";
            criteria.andNameLike(searchName); // 模糊查询
        }
        List<Product> productList = productMapper.selectByExample(productExample);
        // 分页插件详细信息
        PageInfo pageInfo = new PageInfo<Product>(productList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse addProduct(ProductVo productVo) {

        // 默认第一张图片为主图
        if (StringUtils.isNotBlank(productVo.getSubImages())) {
            String[] subImageArray = productVo.getSubImages().split(",");
            if (subImageArray.length > 0) {
                productVo.setMainImage(subImageArray[0]);
            }
        }

        int i = productMapper.insertSelective(productVo);
        if (i == 0) {
            return ServerResponse.createByError("添加商品失败");
        }
        return ServerResponse.createBySuccess("添加商品成功");
    }

    @Override
    public ServerResponse updateProduct(ProductVo productVo) {

        // 默认第一张图片为主图
        if (StringUtils.isNotBlank(productVo.getSubImages())) {
            String[] subImageArray = productVo.getSubImages().split(",");
            if (subImageArray.length > 0) {
                productVo.setMainImage(subImageArray[0]);
            }
        }

        int i = productMapper.updateByPrimaryKeySelective(productVo);
        if (i == 0) {
            return ServerResponse.createByError("更新商品失败");
        }
        return ServerResponse.createBySuccess("更新商品成功");
    }

    /**
     * 根据id查看商品
     *
     * @param productId
     * @return
     */
    @Override
    public ServerResponse<ProductVo> getProductById(Integer productId) {

        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        ProductWithBLOBs productWithBLOBs = productMapper.selectByPrimaryKey(productId);
        if (productWithBLOBs == null) {
            return ServerResponse.createByError();
        }

        ProductVo productVo = new ProductVo();

        BeanUtils.copyProperties(productWithBLOBs, productVo);

        // 根据分类id查询分类对象
        Category category = categoryMapper.selectByPrimaryKey(productVo.getCategoryId());

        if (category != null) {
            productVo.setParentCategoryId(category.getParentId());
        }

        return ServerResponse.createBySuccess(productVo);
    }

    @Override
    public ServerResponse<ProductVo> getPortalProductById(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        ProductWithBLOBs productWithBLOBs = productMapper.selectByPrimaryKey(productId);
        if (productWithBLOBs == null) {
            return ServerResponse.createByErrorMessage("商品不存在");
        }

        if (productWithBLOBs.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("商品已下架或者已删除");
        }

        ProductVo productVo = new ProductVo();
        productVo.setImageHost(fileAddress+"/");
        BeanUtils.copyProperties(productWithBLOBs,productVo);

        return ServerResponse.createBySuccess(productVo);
    }

    @Override
    public ServerResponse<PageInfo> getPortalProductListByCategoryId(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {

//        if(StringUtils.isBlank(keyword) || categoryId==null ){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
//        }

        ProductExample productExample = new ProductExample();
        ProductExample.Criteria criteria = productExample.createCriteria();

        // 分类查询
        if (categoryId != null) {
            criteria.andCategoryIdEqualTo(categoryId);
        }

        // 商品名字模糊查询
        if (StringUtils.isNotBlank(keyword)) {
            String likeStr = new StringBuilder().append("%").append(keyword).append("%").toString();
            criteria.andNameLike(likeStr);
        }


        // 条件模糊查询
        if (Const.ProductListOrderBy.set.contains(orderBy)) {
            String nowOrderBy = orderBy.replace("_", " ");
            productExample.setOrderByClause(nowOrderBy);
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectByExample(productExample);

        // 改造数据
        ArrayList<ProductVo> productVos = new ArrayList<>();

        for (Product product : productList) {
            ProductVo productVo = new ProductVo();
            productVo.setImageHost(fileAddress+"/");
            BeanUtils.copyProperties(product,productVo);
            productVos.add(productVo);
        }


        PageInfo<ProductVo> productPageInfo = new PageInfo<>(productVos);

        return ServerResponse.createBySuccess(productPageInfo);
    }
}
