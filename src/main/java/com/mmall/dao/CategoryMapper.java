package com.mmall.dao;

import com.mmall.pojo.Category;
import com.mmall.pojo.CategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CategoryMapper {
    /**
     *  countByExample
     */
    long countByExample(CategoryExample example);

    /**
     *  deleteByExample
     */
    int deleteByExample(CategoryExample example);

    /**
     *  deleteByPrimaryKey
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  insert
     */
    int insert(Category record);

    /**
     *  insertSelective
     */
    int insertSelective(Category record);

    /**
     *  selectByExample
     */
    List<Category> selectByExample(CategoryExample example);

    /**
     *  selectByPrimaryKey
     */
    Category selectByPrimaryKey(Integer id);

    /**
     *  updateByExampleSelective
     */
    int updateByExampleSelective(@Param("record") Category record, @Param("example") CategoryExample example);

    /**
     *  updateByExample
     */
    int updateByExample(@Param("record") Category record, @Param("example") CategoryExample example);

    /**
     *  updateByPrimaryKeySelective
     */
    int updateByPrimaryKeySelective(Category record);

    /**
     *  updateByPrimaryKey
     */
    int updateByPrimaryKey(Category record);
}