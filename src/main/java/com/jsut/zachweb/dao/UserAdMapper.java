package com.jsut.zachweb.dao;

import com.jsut.zachweb.model.UserAd;

public interface UserAdMapper {
    int deleteByPrimaryKey(String sysId);

    int insert(UserAd record);

    int insertSelective(UserAd record);

    UserAd selectByPrimaryKey(String sysId);

    int updateByPrimaryKeySelective(UserAd record);

    int updateByPrimaryKey(UserAd record);
}