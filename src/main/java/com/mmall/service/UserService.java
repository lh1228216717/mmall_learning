package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

public interface UserService {

    ServerResponse<User> checkUser(String userName,String password);

    ServerResponse<User> insertUser(User user);

    ServerResponse<String> chenkValid(String str,String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username,String question,String answer);

    ServerResponse<String> forgetRestPassword(String username,String password,String token);

    ServerResponse<String> updateUserPassword(String passwordOld,String passwordNew,User user);

    ServerResponse<User> updateUserInfo(User user);

    ServerResponse<User> getUserinfo(Integer userId);
}
