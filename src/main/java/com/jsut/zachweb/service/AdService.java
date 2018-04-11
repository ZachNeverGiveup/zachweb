package com.jsut.zachweb.service;

import com.jsut.zachweb.model.Ad;

public interface AdService {
    /**
     * 发表信息
     * @param ad
     */
    void newAd(Ad ad);

    /**
     * 根据ID删除
     * @param adId
     */
    void deleteByPrimaryKey(Integer adId);

    /**
     * 根据主键更新
     * @param record
     */
    void updateByPrimaryKeySelective(Ad record);

    /**
     * 根据主键查找
     * @param adId
     * @return
     */
    Ad selectByPrimaryKey(Integer adId);

    /**
     * 收藏
     * @param adId
     */
    void collectAdById(Integer userId,Integer adId);

}
