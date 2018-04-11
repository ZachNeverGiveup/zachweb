package com.jsut.zachweb.dao;

import com.jsut.zachweb.model.Ad;
import org.springframework.stereotype.Repository;

@Repository
public interface AdMapper {
    int deleteByPrimaryKey(Integer adId);

    int insert(Ad record);

    int insertSelective(Ad record);

    Ad selectByPrimaryKey(Integer adId);

    int updateByPrimaryKeySelective(Ad record);

    int updateByPrimaryKey(Ad record);

    void collectAdById(Integer userId, Integer adId);
}