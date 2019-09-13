package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User checkUserByNameAndPassword(@Param(value = "userName") String userName, @Param(value = "password")String password);

    int checkUserByUserName(String username);

    int checkUserByUserEmail(String email);

    String selectUserQuestion(String username);

    int checkAnswer(@Param(value = "username")String username,@Param(value = "question")String question,@Param(value = "answer")String answer);

    int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew")String passwordNew);

    int chenkPasswordOld(@Param(value = "passwordOld")String passwordOld,@Param(value = "userId")Integer userId);

    int checkEmailNotByUserId(@Param(value = "userId")Integer userId,@Param(value = "email")String email);
}