package com.mmall.dao;

import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> selectListByIdAndName(@Param("pid") Integer pid,@Param("name") String name);

    List<Product> selectListByCidAndKeyWord(@Param("keyWord") String keyWord,@Param("cids") List<Integer> cids);
}