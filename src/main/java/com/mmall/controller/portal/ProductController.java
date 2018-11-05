package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.ProductWithBLOBs;
import com.mmall.service.IProductService;
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
    public ServerResponse<ProductWithBLOBs> getProductDetailById(Integer productId) {
        return iProductService.getPortalProductById(productId);
    }

    @RequestMapping("/list")
    @ResponseBody
    public ServerResponse getProductList(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return null;
    }


}
