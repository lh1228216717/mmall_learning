package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.CategoryManagerService;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 品类管理
 */
@Controller
@RequestMapping("/manger/category")
public class CategoryManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryManagerService categoryManagerService;
    /*
     * 添加品类接口
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping("/addCategory.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        //校验是否为管理员登录
        if (userService.checkAdminRole(user).isSuccess()) {
          ServerResponse serverResponse=  categoryManagerService.addCategory(categoryName,parentId);
          return    serverResponse;
        }else{
            return ServerResponse.createByErrorMessage("非管理员，无权限操作");
        }
    }

 /**
     * 修改品类名称接口
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "updateCategory.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateCategory(HttpSession session,Integer categoryId,String categoryName){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return categoryManagerService.updateCategory(categoryName,categoryId);
        }else{
            return ServerResponse.createByErrorMessage("非管理员，无权限操作");
        }
    }

    /**
     * 查询当前节点的平级品类
     * @param session
     * @param parentId
     * @return
     */
    @RequestMapping(value = "getCategoryList.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryList(HttpSession session,@RequestParam(value = "parentId",defaultValue = "0") Integer parentId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return categoryManagerService.getCategoryList(parentId);
    }

    /**
         * 递归查询当前节点及子节点接口
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "getDeppCategoryList.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDeppCategoryList(HttpSession session, Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return categoryManagerService.getDeepCategoryList(categoryId);
    }
}
