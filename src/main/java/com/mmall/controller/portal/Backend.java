package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manger/user")
public class Backend {

    @Autowired
    private UserService userService;

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
