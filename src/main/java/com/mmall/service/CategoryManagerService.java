package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface CategoryManagerService {

    ServerResponse addCategory(String categoryName,Integer parentId);

    ServerResponse updateCategory(String categoryName,Integer categoryId);

    ServerResponse<List<Category>> getCategoryList(Integer parentId);

    ServerResponse<List<Integer>> getDeepCategoryList(Integer parentId);
}
