package com.jsut.zachweb.service.impl;

import com.jsut.zachweb.constants.ZachWebConstants;
import com.jsut.zachweb.dao.AdMapper;
import com.jsut.zachweb.dao.CommentMapper;
import com.jsut.zachweb.dao.UserAdMapper;
import com.jsut.zachweb.dao.UserMapper;
import com.jsut.zachweb.dto.AdCollectDTO;
import com.jsut.zachweb.model.Ad;
import com.jsut.zachweb.model.User;
import com.jsut.zachweb.model.UserAd;
import com.jsut.zachweb.service.AdService;
import com.jsut.zachweb.util.DateUtil;
import com.jsut.zachweb.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdServiceImpl implements AdService{

    //日志记录，存到log目录下的sys.log文件中
    private static Logger log = LoggerFactory.getLogger(AdServiceImpl.class);
    @Autowired
    private AdMapper adMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserAdMapper userAdMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void newAd(Ad ad) {
        ad.setAdTypeCode(getAdTypeCode(ad.getAdType()));
        ad.setAdAddTime(DateUtil.getNowDate());
        ad.setAdLastUpdateTime(DateUtil.getNowDate());
        ad.setAdClickNumber(0);
        ad.setAdCollectNumber(0);
        ad.setAdCommentNumber(0);

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

    /**
     * 点击广告查看详细信息
     * @param adId
     * @return
     */
    @Override
    public Ad selectByPrimaryKey(Integer adId) {
        Ad ad = adMapper.selectByPrimaryKey(adId);
        if (null!=ad){
            Integer adClickNumber = ad.getAdClickNumber()+1;
            ad.setAdClickNumber(adClickNumber);
            adMapper.updateByPrimaryKeySelective(ad);
            return ad;
        }else{
            throw new ServiceException("未找到这条信息！");
        }
    }



    @Override
    public List<Ad> selectAdByUserId(Integer userId) {
        return adMapper.selectByUserId(userId);
    }

    @Override
    public void collectAd(Integer adId, User user) {
        if (null==user){
            log.info("collectAd>>>user为空！");
            throw new ServiceException("user为空！");
        }
        try{
            UserAd userAd = new UserAd();
            UUID uuid = UUID.randomUUID();
            String sysId = uuid.toString().replace("-","");
            userAd.setSysId(sysId);
            userAd.setAdId(adId);
            userAd.setUserId(user.getUserId());
            userAd.setCollectTime(DateUtil.getNowDate());
            userAdMapper.insertSelective(userAd);
            Ad ad = adMapper.selectByPrimaryKey(adId);

            //收藏后更新广告的收藏量+1
            Integer adCollectNumber = ad.getAdCollectNumber()+1;
            ad.setAdCollectNumber(adCollectNumber);
            adMapper.updateByPrimaryKeySelective(ad);

            //收藏广告后更新用户的习惯偏好
            if (StringUtils.isEmpty(user.getUserPreference())){
                user.setUserPreference(ad.getAdTypeCode()+ ZachWebConstants.USER_PREFERENCE_SEPARATOR);
            }else {
                user.setUserPreference(user.getUserPreference()+ad.getAdTypeCode()+ ZachWebConstants.USER_PREFERENCE_SEPARATOR);
            }
            userMapper.updateByPrimaryKeySelective(user);
        }catch (Exception e){
            throw new ServiceException("收藏失败！失败原因："+e.getMessage());
        }
    }

    @Override
    public void canceCollectAd(Integer id, User user) {
        if (null==user){
            log.info("cancelCollectAd>>>user为空！");
            throw new ServiceException("user为空！");
        }
        try {
            userAdMapper.deleteByAdIdAndUserId(id,user.getUserId());
        }catch (Exception e){
            log.info("取消收藏失败！失败原因："+e.getMessage());
            throw new ServiceException("取消收藏失败！失败原因："+e.getMessage());
        }
        Ad ad = adMapper.selectByPrimaryKey(id);

        //取消收藏后更新广告的收藏量-1
        Integer adCollectNumber = ad.getAdCollectNumber()-1;
        ad.setAdCollectNumber(adCollectNumber);
        adMapper.updateByPrimaryKeySelective(ad);
    }

    @Override
    public boolean iscollectAd(Integer id, User user) {
        UserAd userAd = userAdMapper.selectByAdIdAndUserId(id, user.getUserId());
        if (null!=userAd){
            return true;
        }
        return false;
    }

    @Override
    public List<Ad> selectAdsByKeyWord(String keyword) {
        List<Ad> ads = new ArrayList<Ad>();
        if (!StringUtils.isEmpty(keyword)){
            ads = adMapper.selectByKeyword(keyword);
            return ads;
        }else{
            return ads;
        }
    }

    /**
     * 查找用户收藏的广告
     * @param userId
     * @return
     */
    @Override
    public List<AdCollectDTO> findUserCollect(Integer userId) {
        List<AdCollectDTO> adCollectDTOList = new ArrayList<AdCollectDTO>();
        List<UserAd> userAds=userAdMapper.selectByUserId(userId);
        if (!CollectionUtils.isEmpty(userAds)){
            for (UserAd userAd:userAds){
                Ad ad = adMapper.selectByPrimaryKey(userAd.getAdId());
                AdCollectDTO adCollectDTO = new AdCollectDTO();
                adCollectDTO.setAd(ad);
                adCollectDTO.setCollectTime(userAd.getCollectTime());
                adCollectDTOList.add(adCollectDTO);
            }
        }
        return adCollectDTOList;
    }

    /**
     * 根据广告类型获取广告类型编码
     * @param adType
     * @return
     */
    public String getAdTypeCode(String adType){
        if (StringUtils.isEmpty(adType)){
            log.info("getAdTypeCode>>>adType为空！");
            throw new ServiceException("广告类型为空！");
        }else{
            log.info("getAdTypeCode>>>adType为{}",adType);
            if (adType.equalsIgnoreCase(ZachWebConstants.GY_AD)){
                return "a";
            }else if (adType.equalsIgnoreCase(ZachWebConstants.ZP_AD)){
                return "b";
            }else{
                return null;
            }
        }
    }
}
