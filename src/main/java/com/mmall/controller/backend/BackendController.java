package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manger/user")
public class BackendController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse login(String userName, String password, HttpSession session){
        ServerResponse<User> sr = userService.checkUser(userName,password);
        if (sr.isSuccess()) {
            if(sr.getData().getRole() == Const.Rore.RORE_ADMIN){
                session.setAttribute(Const.CURRENT_USER,sr.getData());
                return ServerResponse.createBySuccess();
            }else{
                return ServerResponse.createBySuccessMessage("非管理员登陆");
            }
        }
        return sr;
    }
}
