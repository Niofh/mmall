package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.ProductWithBLOBs;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/11/5 22:26
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping("/detail")
    @ResponseBody
    public ServerResponse<ProductVo> getProductDetailById(Integer productId) {
        return iProductService.getPortalProductById(productId);
    }

    /**
     * 前台商列表
     *
     * @param keyword    关键字
     * @param categoryId 商品分类
     * @param pageNum
     * @param pageSize
     * @param orderBy    排序方式
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ServerResponse<PageInfo> getProductList(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                   @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return iProductService.getPortalProductListByCategoryId(keyword, categoryId, pageNum, pageSize, orderBy);
    }


}
