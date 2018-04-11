package com.jsut.zachweb.dao;

import com.jsut.zachweb.model.UserRelation;

public interface UserRelationMapper {
    int deleteByPrimaryKey(String sysId);

    int insert(UserRelation record);

    int insertSelective(UserRelation record);

    UserRelation selectByPrimaryKey(String sysId);

    int updateByPrimaryKeySelective(UserRelation record);

    int updateByPrimaryKey(UserRelation record);
}