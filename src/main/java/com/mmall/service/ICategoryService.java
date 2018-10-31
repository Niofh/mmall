package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {
    /**
     * 添加类别
     * @param categoryName
     * @param parentId  类别的父Id
     * @return
     */
    ServerResponse addCategory(String categoryName, Integer parentId);


    /**
     * 修改品类名字
     * @param categoryName
     * @param categoryId
     * @return
     */
    ServerResponse updateCateGory(String categoryName, Integer categoryId);


    /**
     * 获取平级的品类节点
     * @param parentId 父亲品类id
     * @return
     */
    ServerResponse<List<Category>> getCateGoryByParentId(Integer parentId);

    /**
     * 获取当前分类id及递归子节点categoryId
     * @param categoryId 当前节点分类id
     * @return
     */
    ServerResponse<List<Integer>>  getDeepCateGory(Integer categoryId);
}
