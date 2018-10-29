package com.mmall.dao;

import com.mmall.pojo.User;
import com.mmall.pojo.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    /**
     *  countByExample
     */
    long countByExample(UserExample example);

    /**
     *  deleteByExample
     */
    int deleteByExample(UserExample example);

    /**
     *  deleteByPrimaryKey
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  insert
     */
    int insert(User record);

    /**
     *  insertSelective
     */
    int insertSelective(User record);

    /**
     *  selectByExample
     */
    List<User> selectByExample(UserExample example);

    /**
     *  selectByPrimaryKey
     */
    User selectByPrimaryKey(Integer id);

    /**
     *  updateByExampleSelective
     */
    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    /**
     *  updateByExample
     */
    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    /**
     *  updateByPrimaryKeySelective
     */
    int updateByPrimaryKeySelective(User record);

    /**
     *  updateByPrimaryKey
     */
    int updateByPrimaryKey(User record);
}