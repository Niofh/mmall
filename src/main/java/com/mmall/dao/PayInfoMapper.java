package com.mmall.dao;

import com.mmall.pojo.PayInfo;
import com.mmall.pojo.PayInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PayInfoMapper {
    /**
     *  countByExample
     */
    long countByExample(PayInfoExample example);

    /**
     *  deleteByExample
     */
    int deleteByExample(PayInfoExample example);

    /**
     *  deleteByPrimaryKey
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  insert
     */
    int insert(PayInfo record);

    /**
     *  insertSelective
     */
    int insertSelective(PayInfo record);

    /**
     *  selectByExample
     */
    List<PayInfo> selectByExample(PayInfoExample example);

    /**
     *  selectByPrimaryKey
     */
    PayInfo selectByPrimaryKey(Integer id);

    /**
     *  updateByExampleSelective
     */
    int updateByExampleSelective(@Param("record") PayInfo record, @Param("example") PayInfoExample example);

    /**
     *  updateByExample
     */
    int updateByExample(@Param("record") PayInfo record, @Param("example") PayInfoExample example);

    /**
     *  updateByPrimaryKeySelective
     */
    int updateByPrimaryKeySelective(PayInfo record);

    /**
     *  updateByPrimaryKey
     */
    int updateByPrimaryKey(PayInfo record);
}