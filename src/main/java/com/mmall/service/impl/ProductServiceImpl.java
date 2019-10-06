package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.CategoryManagerService;
import com.mmall.service.ProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryManagerService categoryManagerService;

    @Autowired
    CategoryMapper categoryMapper;

    public ServerResponse addAndUpdateProduct(Product product){
        if (product != null) {
            if(StringUtils.isNoneBlank(product.getSubImages())){
                String[] str = product.getSubImages().split(",");
                if(str.length>0){
                    product.setMainImage(str[0]);
                }
            }
            int valCount =0 ;
            if (product.getId() != null) {
                //表示更新
                valCount =  productMapper.updateByPrimaryKey(product);
            }else{
                //表示添加
                valCount = productMapper.insert(product);
            }
            if(valCount>0){
                return ServerResponse.createBySuccess();
            }
            return ServerResponse.createByError();
        }else{
            return ServerResponse.createByErrorMessage("添加或更新参数不正确");
        }
    }

    public ServerResponse updateProductSatus(Integer pid,Integer status){
        if(pid == null || status == null){
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(pid);
        product.setStatus(status);
        int valCount = productMapper.updateByPrimaryKeySelective(product);
        if(valCount > 0){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    public ServerResponse selectProductDetail(Integer pid){
        if (pid == null) {
            return ServerResponse.createByError();
        }
        Product product = productMapper.selectByPrimaryKey(pid);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架,或被删除");
        }
        ProductDetailVo  pvo = this.getToProductDatailVo(product);
        return ServerResponse.createBySuccess(pvo);
    }
    public ProductDetailVo getToProductDatailVo(Product product){
        ProductDetailVo pvo = new ProductDetailVo();
        pvo.setId(product.getId());
        pvo.setCategoryId(product.getCategoryId());
        pvo.setName(product.getName());
        pvo.setSubtitle(product.getSubtitle());
        pvo.setMainImage(product.getMainImage());
        pvo.setDetail(product.getDetail());
        pvo.setPrice(product.getPrice());
        pvo.setStatus(product.getStatus());
        pvo.setStock(product.getStock());
        pvo.setCreateTime(DateTimeUtil.dateToStr(new Date()));
        pvo.setUpdateTime(DateTimeUtil.dateToStr(new Date()));
        pvo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            pvo.setParentCategoryId(0);
        }else{
            pvo.setParentCategoryId(category.getParentId());
        }
        return pvo;
    }

    public ServerResponse getProductListDatailVo(Integer pageNumber,Integer pageSize){
        PageHelper.startPage(pageNumber,pageSize);
        List<Product> products = productMapper.selectList();
        List<ProductListVo> productListVos = products.stream().map(e->this.getToProductListVo(e)).collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);
    }

    public ProductListVo getToProductListVo(Product product){
        ProductListVo pvo = new ProductListVo();
        pvo.setId(product.getId());
        pvo.setCategoryId(product.getCategoryId());
        pvo.setName(product.getName());
        pvo.setSubtitle(product.getSubtitle());
        pvo.setMainImage(product.getMainImage());
        pvo.setPrice(product.getPrice());
        pvo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        pvo.setStatus(product.getStatus());
        return pvo;
    }

   public  ServerResponse serachProduct(Integer pid,String name,Integer pageNumber,Integer pageSize){
       PageHelper.startPage(pageNumber,pageSize);
       StringBuffer sb = null;
       if(StringUtils.isNoneBlank(name)){
           sb = new StringBuffer().append("%").append(name).append("%");
       }
       List<Product> products = productMapper.selectListByIdAndName(pid,sb.toString());
       List<ProductListVo> productListVos = products.stream().map(e->this.getToProductListVo(e)).collect(Collectors.toList());
       PageInfo pageInfo = new PageInfo(products);
       pageInfo.setList(productListVos);
       return ServerResponse.createBySuccess(pageInfo);
   }

    public ServerResponse getProductDetail(Integer pid){
        if (pid == null) {
            return ServerResponse.createByError();
        }
        Product product = productMapper.selectByPrimaryKey(pid);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架,或被删除");
        }
        if(product.getStatus() != Const.ProductEnum.ON_SALE.getCode()){
            return ServerResponse.createByErrorMessage("产品已下架,或被删除");
        }
        ProductDetailVo  pvo = this.getToProductDatailVo(product);
        return ServerResponse.createBySuccess(pvo);
    }

    public ServerResponse serachKeyWordAndCid(String keyWord,Integer cid,Integer pageNum,Integer pageSize,String orderby){
        if(StringUtils.isBlank(keyWord) && cid == null){
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryInts = Lists.newArrayList();
        if(cid != null){
            Category category = categoryMapper.selectByPrimaryKey(cid);
            if(category==null && StringUtils.isBlank(keyWord)){
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> listVos = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(listVos);
                return ServerResponse.createBySuccess();
            }
            categoryInts = categoryManagerService.getDeepCategoryList(category.getId()).getData();
        }
        if(StringUtils.isNoneBlank(keyWord)){
            keyWord = new StringBuilder().append("%").append(keyWord).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNoneBlank(orderby)){
            if(Const.productPriceConst.price_set.contains(orderby)){
                String[] sp = orderby.split("-");
                PageHelper.orderBy(sp[0]+" "+sp[1]);
            }
        }
        List<Product> products = productMapper.selectListByCidAndKeyWord(StringUtils.isBlank(keyWord)?null:keyWord,categoryInts.size()<=0?null:categoryInts);
        List<ProductListVo> productListVos = products.stream().map(e->getToProductListVo(e)).collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
