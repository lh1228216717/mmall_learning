package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.CategoryManagerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("categoryManagerService")
public class CategoryManagerServiceImpl implements CategoryManagerService {

    Logger logger = LoggerFactory.getLogger(CategoryManagerServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;


        public ServerResponse addCategory(String categoryName, Integer parentId){
            if(parentId == null|| StringUtils.isBlank(categoryName)){
                return ServerResponse.createByErrorMessage("参数错误");
            }
            Category category = new Category();
            category.setName(categoryName);
            category.setParentId(parentId);
            category.setStatus(true);
            int rwoCount = categoryMapper.insert(category);
            if(rwoCount>0){
                return ServerResponse.createBySuccess();
            }
            return ServerResponse.createByError();
        }

        public ServerResponse updateCategory(String categoryName,Integer categoryId){
            if(categoryId == null|| StringUtils.isBlank(categoryName)){
                return ServerResponse.createByErrorMessage("参数错误");
            }
            Category category = new Category();
            category.setId(categoryId);
            category.setName(categoryName);
            int rowValue = categoryMapper.updateByPrimaryKeySelective(category);
            if(rowValue>0){
                return ServerResponse.createBySuccess();
            }
            return ServerResponse.createByErrorMessage("更新失败");
        }

        public ServerResponse<List<Category>> getCategoryList(Integer parentId){
            List<Category> list = categoryMapper.getCategoryList(parentId);
            if(list.isEmpty()){
                logger.info("未找到当前分类的子品类");
            }
            return ServerResponse.createBySuccess(list);
        }

        public ServerResponse<List<Integer>> getDeepCategoryList(Integer parentId){
            Set<Category> categorySet = Sets.newHashSet();
            findCategoryDeep(categorySet,parentId);
            List<Integer> categoryIdList = Lists.newArrayList();
            if(parentId != null){
                for(Category categoryItem : categorySet){
                    categoryIdList.add(categoryItem.getId());
                }
            }
            return ServerResponse.createBySuccess(categoryIdList);
        }

        public Set<Category> findCategoryDeep(Set<Category> categories,Integer parentId){
            Category category = categoryMapper.selectByPrimaryKey(parentId);
            if (category != null) {
                categories.add(category);
            }
            List<Category> list = categoryMapper.getCategoryList(parentId);
            for (Category category1:list){
                findCategoryDeep(categories,category1.getId());
            }
            return categories;
        }

}

