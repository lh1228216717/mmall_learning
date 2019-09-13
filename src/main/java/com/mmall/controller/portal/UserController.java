package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private  UserService userService;

    /**
     * 用户登录
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String userName, String password, HttpSession session){
        ServerResponse<User> sr  =  userService.checkUser(userName,password);
        if(sr.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,sr.getData());
        }
        return sr;
    }

    /**
     * 用户退出
     * @param session
     * @return
     */
    @RequestMapping(value = "loginOut.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> loginOut(HttpSession session){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value = "registor.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> registor(User user){
            return userService.insertUser(user);
    }

    /**
     * 用户校验接口
     * @param str 校验字符串
     * @param type 0-username 1-email
     * @return
     */
    @RequestMapping(value = "checkValid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return userService.chenkValid(str,type);
    }

    /**
     * 获取用户信息接口
     * @param session
     * @return
     */
    @RequestMapping(value = "getUser.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUser(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("没有对应的用户信息");
    }
//********************************************  非登录状态下的重置密码
    /**
     * 根据用户名查找用户问题接口
     * @param username
     * @return
     */
    @RequestMapping(value = "forgetGetQuestion.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return userService.selectQuestion(username);
    }

    /**
     * 验证答案接口
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forgetCheckAnswer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return userService.checkAnswer(username,question,answer);
    }

    /**
     * 重置密码接口
     * @param username
     * @param password
     * @param token
     * @return
     */
    @RequestMapping(value = "forgetRestPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username,String password,String token){
        return userService.forgetRestPassword(username,password,token);
    }


    /**
     * 登录状态下重置密码
     * 重置密码接口
     * @param passwordOld
     * @param passwordNew
     * @param session
     * @return
     */
    @RequestMapping(value = "resetPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse resetPassword(String passwordOld,String passwordNew,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccessMessage("用户未登录");
        }
        return userService.updateUserPassword(passwordOld,passwordNew,user);
    }

    /**
     * 用户信息更新接口
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "updateUserInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateUserInfo(HttpSession session,User user){
        User currecUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currecUser == null) {
            return ServerResponse.createBySuccessMessage("用户未登录");
        }
        user.setId(currecUser.getId());
        user.setUsername(user.getUsername());
        ServerResponse sr =  userService.updateUserInfo(user);
        if (sr.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,sr.getData());
        }
        return sr;
    }

    /**
     * 获取用户信息接口
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "getUserinfo.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getUserinfo(HttpSession session,User user){
        User currecUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currecUser == null) {
            return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return userService.getUserinfo(currecUser.getId());
    }
}
