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
    public void deleteByPrimaryKey(Integer adId,User user) {
        Ad adSelectByPrimaryKey = adMapper.selectByPrimaryKey(adId);
        if (null==adSelectByPrimaryKey){
            throw new ServiceException("没找到这条信息！");
        }
        //如果广告不是本人或该用户不是管理员
        if (!adSelectByPrimaryKey.getUser().getUserId().equals(user.getUserId())&&user.getUserGrade()<2){
            throw new ServiceException("你没有权限删除广告！");
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

    @Override
    public List<Ad> findAllAds() {
        return adMapper.selectAllAds();
    }

    @Override
    public List<Ad> findAdsByPage(String page, String limit) {
        Integer pageSize = Integer.parseInt(limit);
        Integer pageStart = pageSize * (Integer.parseInt(page) - 1);
        return adMapper.selectAdsByPage(pageStart,pageSize);
    }

    /**
     * 算法推荐
     * @param user
     * @return
     */
    @Override
    public List<Ad> findUserLikeAdList(User user) {
        List<Ad> adList = new ArrayList<Ad>();
        if (null==user){
            return adList;
        }
        String[] preferences = user.getUserPreference().split(ZachWebConstants.USER_PREFERENCE_SEPARATOR);
        log.info(preferences.toString());
        String maxTypefromPreference = findMaxTypefromPreference(preferences);
        String[] types = maxTypefromPreference.split(ZachWebConstants.USER_PREFERENCE_SEPARATOR);
        if (types.length==1){
            log.info("用户最喜欢的广告类型是：{}",types[0]);
            List<Ad> indexAdListByPage = findIndexAdListByPage(types[0], "a.ad_click_number", "1", "4");
            adList.addAll(indexAdListByPage);
        }else{
            for (String type:types){
                log.info("用户最喜欢的广告类型们是这些：{}",type);
                List<Ad> indexAdListByPage = findIndexAdListByPage(type, "a.ad_click_number", "1", "2");
                adList.addAll(indexAdListByPage);
            }
        }

        for (Ad ad:adList){
            log.info("*********用户猜你喜欢的广告是：********"+ad.getAdTitle()+ad.getAdType()+ad.getAdClickNumber());
        }
        return adList;
    }

    @Override
    public List<Ad> findIndexAdListByPage(String adType,String sortType, String page, String limit) {
        if (StringUtils.isEmpty(limit)){
            limit="10";
        }
        if (StringUtils.isEmpty(page)){
            page="1";
        }
        Integer pageSize = Integer.parseInt(limit);
        Integer pageStart = pageSize * (Integer.parseInt(page) - 1);
        if(StringUtils.isEmpty(sortType)||sortType.equalsIgnoreCase("1")){
            sortType = "a.ad_last_update_time";
        }else if (sortType.equalsIgnoreCase("2")){
            sortType = "a.ad_comment_number";
        }
        List<Ad> adList=adMapper.selectAdsByPageAndAdType(adType,sortType,pageStart,pageSize);
        return adList;
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
            }else if (adType.equalsIgnoreCase(ZachWebConstants.FC_AD)){
                return "c";
            }else if (adType.equalsIgnoreCase(ZachWebConstants.ES_AD)){
                return "d";
            }else if (adType.equalsIgnoreCase(ZachWebConstants.CW_AD)){
                return "e";
            }else if (adType.equalsIgnoreCase(ZachWebConstants.SW_AD)){
                return "f";
            }else if (adType.equalsIgnoreCase(ZachWebConstants.FW_AD)){
                return "g";
            }else if (adType.equalsIgnoreCase(ZachWebConstants.LY_AD)){
                return "h";
            }else if (adType.equalsIgnoreCase(ZachWebConstants.CY_AD)){
                return "i";
            }else{
                return null;
            }
        }
    }

    /**
     * 从用户偏好数组中找出出现次数最多的
     * @param strs
     * @return
     */
    public String findMaxTypefromPreference(String[] strs){
        int max=1;
        String result="";
        for(int i=0;i<strs.length-1;i++)
        {
            int count=1;
            for(int j=i+1;j<strs.length;j++)
            {
                if(strs[i].equals(strs[j]))
                    count++;
            }
            if(max<count)
                max=count;
        }
        log.info("重复最多次数为："+max);
        for(int i=0;i<strs.length-1;i++)
        {
            int count=1;
            for(int j=i+1;j<strs.length;j++)
            {
                if(strs[i].equals(strs[j]))
                    count++;
            }
            if(count==max){
                log.info("重复最多次("+max+")的字符串为："+strs[i]);
                result=StringUtils.isEmpty(result)?strs[i]:(result+ZachWebConstants.USER_PREFERENCE_SEPARATOR+strs[i]);
            }
        }
        return result;
    }
}
