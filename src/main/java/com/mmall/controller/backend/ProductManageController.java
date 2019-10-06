package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.FileService;
import com.mmall.service.ProductService;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manger/product")
public class ProductManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/insertOrUpdateProduct.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse insertOrUpdateProduct(HttpSession session, Product product){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.addAndUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMessage("非管理员，无权限操作");
        }
    }

    @RequestMapping(value = "/setSalesStatus.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSalesStatus(HttpSession session,Integer pid,Integer status){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (userService.checkAdminRole(user).isSuccess()) {
          return productService.updateProductSatus(pid,status);
        }else{
            return ServerResponse.createByErrorMessage("非管理员，无权限操作");
        }
    }
    @RequestMapping(value = "/getDetail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session,Integer pid){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.selectProductDetail(pid);
        }else{
            return ServerResponse.createByErrorMessage("非管理员，无权限操作");
        }
    }

    @RequestMapping(value = "/getDetailList.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetailList(HttpSession session,  @RequestParam(value = "pageNumber",defaultValue = "1") Integer pageNumber,@RequestParam(value = "pageSize",defaultValue = "10")  Integer pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.getProductListDatailVo(pageNumber,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("非管理员，无权限操作");
        }
    }

    @RequestMapping(value = "/productSearch.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, Integer pid,
                                        String name,
                                        @RequestParam(value = "pageNumber",defaultValue = "1") Integer pageNumber,
                                        @RequestParam(value = "pageSize",defaultValue = "10" )  Integer pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.serachProduct(pid,name,pageNumber,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("非管理员，无权限操作");
        }
    }

    @RequestMapping(value = "/upload.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "fileUp_name",required = false) MultipartFile file){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            String path = session.getServletContext().getRealPath("upload");
            return fileService.fileUp(file,path);
        }else{
            return ServerResponse.createByErrorMessage("非管理员，无权限操作");
        }
    }
}
