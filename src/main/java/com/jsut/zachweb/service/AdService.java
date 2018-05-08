package com.jsut.zachweb.service;

import com.jsut.zachweb.dto.AdCollectDTO;
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
    void deleteByPrimaryKey(Integer adId,User user);

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

    /**
     * 用户是否收藏该广告
     * @param id
     * @param user
     * @return
     */
    boolean iscollectAd(Integer id, User user);

    List<Ad> selectAdsByKeyWord(String keyword);

    List<AdCollectDTO> findUserCollect(Integer userId);

    List<Ad> findAllAds();

    List<Ad> findAdsByPage(String page, String limit);

    List<Ad> findUserLikeAdList(User user);

    /**
     * 根据广告类型、页码、每页显示数量来查找广告
     * @param adType
     * @param sortType
     * @param page
     * @param limit
     * @return
     */
    List<Ad> findIndexAdListByPage(String adType,String sortType,String page,String limit);
}
