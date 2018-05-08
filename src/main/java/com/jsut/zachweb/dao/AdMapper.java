package com.jsut.zachweb.dao;

import com.jsut.zachweb.model.Ad;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdMapper {
    int deleteByPrimaryKey(Integer adId);

    int insert(Ad record);

    int insertSelective(Ad record);

    Ad selectByPrimaryKey(Integer adId);

    int updateByPrimaryKeySelective(Ad record);

    int updateByPrimaryKey(Ad record);

    List<Ad> selectByUserId(Integer userId);

    List<Ad> selectByKeyword(@Param("keyword") String keyword);

    List<Ad> selectAllAds();

    List<Ad> selectAdsByPage(@Param("pageStart") Integer pageStart, @Param("pageSize") Integer PageSize);

    List<Ad> selectAdsByPageAndAdType(@Param("adType") String adType,@Param("sortType") String sortType,@Param("pageStart") Integer pageStart, @Param("pageSize") Integer PageSize);
}