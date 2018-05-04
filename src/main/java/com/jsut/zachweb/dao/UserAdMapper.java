package com.jsut.zachweb.dao;

import com.jsut.zachweb.model.UserAd;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAdMapper {
    int deleteByPrimaryKey(String sysId);

    int insert(UserAd record);

    int insertSelective(UserAd record);

    UserAd selectByPrimaryKey(String sysId);

    int updateByPrimaryKeySelective(UserAd record);

    int updateByPrimaryKey(UserAd record);

    int deleteByAdIdAndUserId(@Param("adId") Integer adId, @Param("userId") Integer userId);

    UserAd selectByAdIdAndUserId(@Param("adId") Integer adId, @Param("userId") Integer userId);

    List<UserAd> selectByUserId(Integer userId);
}