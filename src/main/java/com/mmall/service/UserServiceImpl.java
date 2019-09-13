package com.mmall.service;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    public ServerResponse<User> checkUser(String userName, String password){
        User user= null;
        password = MD5Util.MD5EncodeUtf8(password);
        user = userMapper.checkUserByNameAndPassword(userName,password);
        if(user == null){
            ResponseCode responseCode = ResponseCode.NEED_LOGIN;
            return  ServerResponse.createByErrorMessage(responseCode.getCode(),responseCode.getDesc());
        }else{
            user.setPassword(StringUtils.EMPTY);//密码置为空
            return  ServerResponse.createBySuccess(user);
        }
    }

    public ServerResponse<User> insertUser(User user){
        ServerResponse sr = this.chenkValid(user.getUsername(),Const.USERNAME);
        if (!sr.isSuccess()) {
            return sr;
        }
        sr = this.chenkValid(user.getEmail(),Const.EMAIL);
        if (!sr.isSuccess()) {
            return sr;
        }
        user.setRole(Const.Rore.RORE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resVal =  userMapper.insert(user);
        if(resVal == 1){
            return ServerResponse.createBySuccessMessage("注册成功");
        }else{
            return ServerResponse.createByError();
        }
    }

    public ServerResponse<String> chenkValid(String str,String type){
        if(!StringUtils.isNotBlank(str)) return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        int count = 0;
        if(Const.USERNAME.equals(type)){
            count  = userMapper.checkUserByUserName(str);
            if(count>0)   return ServerResponse.createByErrorMessage("用户名已存在");
        }
        if(Const.EMAIL.equals(type)){
            count = userMapper.checkUserByUserEmail(str);
            if(count>0) return ServerResponse.createByErrorMessage("email已存在");
        }
        return ServerResponse.createBySuccess("校验成功");
    }

    public ServerResponse<String> selectQuestion(String username){
        ServerResponse sr = this.chenkValid(username,Const.USERNAME);
        if(sr.isSuccess()) return ServerResponse.createByErrorMessage("用户名不存在");
       String question = userMapper.selectUserQuestion(username);
       if(StringUtils.isNotBlank(question)){
           return ServerResponse.createBySuccess(question);
       }
        return ServerResponse.createByErrorMessage("没有查找到此用户的问题");
    }

    public ServerResponse<String> checkAnswer(String username,String question,String answer){
       int resCount = userMapper.checkAnswer(username,question,answer);
       if(resCount>0){
           String uuid = UUID.randomUUID().toString();
           TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,uuid);
           return ServerResponse.createBySuccess(uuid);
       }
       return ServerResponse.createByErrorMessage("验证答案错误");
    }

    public ServerResponse<String> forgetRestPassword(String username,String password,String token){
        ServerResponse sr = this.chenkValid(username,Const.USERNAME);
        if(sr.isSuccess()) return ServerResponse.createByErrorMessage("用户名不存在");
        if(StringUtils.isBlank(token))  return ServerResponse.createByErrorMessage("token为空");
        String tokenCacheKey = TokenCache.TOKEN_PREFIX+username;
        String tokenValue = TokenCache.getKey(tokenCacheKey);
        if(StringUtils.equals(TokenCache.getKey(tokenCacheKey),token)){
            String md5Paw = MD5Util.MD5EncodeUtf8(password);
            int recCount =0;
            try {
                recCount  = userMapper.updatePasswordByUsername(username,md5Paw);
            }catch (Exception e){
                e.printStackTrace();
            }

            if(recCount>0){
                return ServerResponse.createBySuccessMessage("密码重置成功");
            }else{
                return ServerResponse.createByErrorMessage("修改失败");
            }
        }else {
            return ServerResponse.createBySuccessMessage("token过期，请重新获取");
        }
    }

    public ServerResponse<String> updateUserPassword(String passwordOld,String passwordNew,User user){
        passwordOld = MD5Util.MD5EncodeUtf8(passwordOld);
        int resCount = userMapper.chenkPasswordOld(passwordOld,user.getId());
        if(resCount<=0) return ServerResponse.createBySuccessMessage("旧密码错误");
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        resCount = userMapper.updateByPrimaryKeySelective(user);
        if(resCount>0){
            return ServerResponse.createBySuccessMessage("密码重置成功");
        }else{
            return ServerResponse.createByErrorMessage("密码重置失败");
        }
    }

    public ServerResponse<User> updateUserInfo(User user){
        int resCount = userMapper.checkEmailNotByUserId(user.getId(),user.getEmail());
        if(resCount>0) return ServerResponse.createByErrorMessage("邮箱被占用");
        User newUser = new User();
        newUser.setAnswer(user.getAnswer());
        newUser.setQuestion(user.getQuestion());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setId(user.getId());
        resCount = userMapper.updateByPrimaryKeySelective(newUser);
        if(resCount>0){
            return ServerResponse.createByError("更新成功",newUser);
        }else{
            return ServerResponse.createByErrorMessage("用户信息更新失败");
        }
    }

    public ServerResponse<User> getUserinfo(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createBySuccessMessage("用户不存在");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }
}
