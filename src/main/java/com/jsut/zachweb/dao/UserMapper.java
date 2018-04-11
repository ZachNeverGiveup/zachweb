package com.jsut.zachweb.dao;

import com.jsut.zachweb.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    User selectByUserName(String userName);

    User selectByUserNameAndPwd(User user);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}