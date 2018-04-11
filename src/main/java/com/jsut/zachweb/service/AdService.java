package com.jsut.zachweb.service;

import com.jsut.zachweb.model.Ad;

public interface AdService {
    void newAd(Ad ad);
    void deleteByPrimaryKey(Integer adId);
    void updateByPrimaryKeySelective(Ad record);
    Ad selectByPrimaryKey(Integer adId);

}
