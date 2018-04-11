package com.jsut.zachweb.service.impl;

import com.jsut.zachweb.dao.AdMapper;
import com.jsut.zachweb.dao.CommentMapper;
import com.jsut.zachweb.model.Ad;
import com.jsut.zachweb.service.AdService;
import com.jsut.zachweb.util.DateUtil;
import com.jsut.zachweb.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdServiceImpl implements AdService{

    //日志记录，存到log目录下的sys.log文件中
    private static Logger log = LoggerFactory.getLogger(AdServiceImpl.class);
    @Autowired
    private AdMapper adMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public void newAd(Ad ad) {
        ad.setAdAddTime(DateUtil.getNowDate());
        ad.setAdLastUpdateTime(DateUtil.getNowDate());
        adMapper.insert(ad);
    }

    @Override
    public void deleteByPrimaryKey(Integer adId) {
        Ad adSelectByPrimaryKey = adMapper.selectByPrimaryKey(adId);
        if (null==adSelectByPrimaryKey){
            throw new ServiceException("没找到这条信息！");
        }
        adMapper.deleteByPrimaryKey(adId);
        commentMapper.deleteByAdId(adId);
    }

    @Override
    public void updateByPrimaryKeySelective(Ad record) {
        adMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Ad selectByPrimaryKey(Integer adId) {
        Ad ad = adMapper.selectByPrimaryKey(adId);
        if (null!=ad){
            return ad;
        }else{
            throw new ServiceException("未找到这条信息！");
        }
    }

    @Override
    public void collectAdById(Integer userId, Integer adId) {
        Ad adSelectByPrimaryKey = adMapper.selectByPrimaryKey(adId);
        if (null==adSelectByPrimaryKey){
            throw new ServiceException("没找到这条信息！");
        }
        adMapper.collectAdById(userId,adId);
    }
}
