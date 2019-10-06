package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

public interface ProductService {

    ServerResponse addAndUpdateProduct(Product product);

    ServerResponse updateProductSatus(Integer pid,Integer status);

    ServerResponse selectProductDetail(Integer pid);

    ServerResponse getProductListDatailVo(Integer pageNumber,Integer pageSize);

    ServerResponse serachProduct(Integer pid,String name,Integer pageNumber,Integer pageSize);

    ServerResponse getProductDetail(Integer pid);

    ServerResponse serachKeyWordAndCid(String keyWord,Integer cid,Integer pageNum,Integer pageSize,String orderby);
}
