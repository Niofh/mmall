package com.mmall.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mmall.pojo.ProductWithBLOBs;

/**
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/11/5 17:53
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVo extends ProductWithBLOBs {

    // 上级分类id
    private Integer parentCategoryId;
    // 图片ip地址
    private String imageHost;

    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
