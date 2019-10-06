package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/getDetail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(Integer pid){
            return productService.getProductDetail(pid);
    }
    @RequestMapping(value = "/getKeyWordAndCid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getKeyWordAndCid(@RequestParam(value = "keyWord",required = false) String keyWord,
                                            @RequestParam(value="cid",required = false)Integer cid,
                                            @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                            @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize,
                                            @RequestParam(value = "priceOrder",defaultValue = "")String priceOrder){
        return productService.serachKeyWordAndCid(keyWord, cid, pageNum, pageSize, priceOrder);
    }
}
