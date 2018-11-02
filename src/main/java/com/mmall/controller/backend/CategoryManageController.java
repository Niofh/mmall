package com.mmall.controller.backend;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/manage/category")
@Controller
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;



    @RequestMapping(value="/add_category")
    @ResponseBody
    public ServerResponse addCateGory(String categoryName, @RequestParam(value = "parentId",defaultValue = "0") Integer parentId) {

        return iCategoryService.addCategory(categoryName,parentId);
    }



    @RequestMapping(value="/set_category_name")
    @ResponseBody
    public ServerResponse updateCateGory(String categoryName, Integer categoryId) {

        return iCategoryService.updateCateGory(categoryName,categoryId);
    }

    /**
     * 获取平级的品类节点
     * @param categoryId 父亲品类id
     * @return
     */
    @RequestMapping(value="/get_category",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getCateGory(Integer categoryId) {

        return iCategoryService.getCateGoryByParentId(categoryId);
    }

    /**
     * 获取当前分类id及递归子节点categoryId
     * @param categoryId 当前节点分类id
     * @return
     */
    @RequestMapping(value="/get_deep_category",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Integer>>  getDeepCateGory(Integer categoryId) {

        return iCategoryService.getDeepCateGory(categoryId);
    }

}
