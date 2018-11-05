package com.mmall.vo;

import com.mmall.pojo.ProductWithBLOBs;

/**
 * @Description:
 * @Author: Nicofh
 * @Date: 2018/11/5 17:53
 */
public class ProductVo extends ProductWithBLOBs {

    // 上级分类id
    private Integer  parentCategoryId;

    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
}
