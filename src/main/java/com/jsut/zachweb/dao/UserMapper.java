package com.jsut.zachweb.dao;

import com.jsut.zachweb.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    User selectByUserName(String userName);

    User selectByUserNameAndPwd(User user);

    User selectByEmail(String userEmail);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> selectAllUser();

    List<User> findUserByPage(@Param("pageStart") Integer pageStart, @Param("pageSize") Integer PageSize);

    List<User> findPendingUserByPage(@Param("pageStart") Integer pageStart, @Param("pageSize") Integer PageSize);

    List<User> selectPendingUser();
}