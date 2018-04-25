package com.jsut.zachweb.service;

import com.jsut.zachweb.model.Ad;
import com.jsut.zachweb.model.User;

import java.util.List;

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
     * 查找用户发表过的广告
     * @param userId
     * @return
     */
    List<Ad> selectAdByUserId(Integer userId);

    /**
     * 用户收藏广告
     * @param adId
     * @param user
     */
    void collectAd(Integer adId,User user);

    /**
     * 用户取消收藏广告
     * @param id
     * @param user
     */
    void canceCollectAd(Integer id, User user);

    boolean iscollectAd(Integer id, User user);
}
