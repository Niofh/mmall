package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.CategoryExample;
import com.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {

        if (categoryName == null || parentId == null) {

            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }


        Category category = new Category();

        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);

        int i = categoryMapper.insertSelective(category);

        if (i > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }

        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse updateCateGory(String categoryName, Integer categoryId) {

        if (categoryName == null || categoryId == null) {

            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }

        Category category = new Category();

        category.setName(categoryName);
        category.setId(categoryId);

        int i = categoryMapper.updateByPrimaryKeySelective(category);

        if (i > 0) {
            return ServerResponse.createBySuccessMessage("修改品类成功");
        }

        return ServerResponse.createByErrorMessage("修改品类失败");
    }

    @Override
    public ServerResponse<List<Category>> getCateGoryByParentId(Integer parentId) {
        if (parentId == null) {

            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }

        CategoryExample categoryExample = new CategoryExample();
        categoryExample.createCriteria().andParentIdEqualTo(parentId);
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse<List<Integer>> getDeepCateGory(Integer categoryId) {
        if (categoryId == null) {

            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }

        Category category = categoryMapper.selectByPrimaryKey(categoryId);

        if (category == null) {
            return ServerResponse.createByErrorMessage("没有此分类");
        }

        ArrayList<Category> categoryArrayList = new ArrayList<>();

        categoryArrayList.add(category);

        // 所有深层分类
        List<Category> listChildren = this.getChildrenNodeByParentId(categoryArrayList, categoryId);

        ArrayList<Integer> integerArrayList = new ArrayList<>();

        for (Category item : listChildren) {
            integerArrayList.add(item.getId());
        }


        return ServerResponse.createBySuccess(integerArrayList);
    }

    /**
     * 根据parentId查找所有子分类节点（递归）
     *
     * @param list     空数组
     * @param parentId 节点父id
     * @return
     */
    private List<Category> getChildrenNodeByParentId(List<Category> list, Integer parentId) {

        CategoryExample categoryExample = new CategoryExample();
        categoryExample.createCriteria().andParentIdEqualTo(parentId);
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        list.addAll(categoryList);

        if (categoryList.size() > 0) {
            for (Category category : categoryList) {
                if (category != null) {
                    this.getChildrenNodeByParentId(list, category.getId());
                }
            }
        }


        return list;


    }

}
