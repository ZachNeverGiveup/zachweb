package com.jsut.zachweb.controller;

import com.jsut.zachweb.model.Ad;
import com.jsut.zachweb.service.AdService;
import com.jsut.zachweb.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping(value = "/ad")
public class AdController {

    //日志记录，存到log目录下的sys.log文件中
    private static Logger log = LoggerFactory.getLogger(AdController.class);

    @Autowired
    private AdService adService;

    /**
     * 新增广告信息
     * @param userId
     * @param type
     * @param typeCode
     * @param title
     * @param html
     * @param text
     * @param picURL
     * @param price
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/newAd",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResult newAd(Integer userId,
                            String type,String typeCode,
                            String title,String html,
                            String text,String picURL,
                            String price,
                            Date startTime,Date endTime){
        log.info(">>>userid:"+userId);
        log.info(">>>type:"+type);
        log.info(">>>typeCode:"+typeCode);
        log.info(">>>title:"+title);
        log.info(">>>html:"+html);
        log.info(">>>text:"+text);
        log.info(">>>pic:"+picURL);
        log.info(">>>price:"+price);
        log.info(">>>startTime:"+startTime);
        log.info(">>>endTime:"+endTime);
        Ad ad = new Ad();
        ad.setAdUserId(userId);
        ad.setAdType(type);
        ad.setAdTypeCode(typeCode);
        ad.setAdTitle(title);
        ad.setAdHtml(html);
        ad.setAdText(text);
        ad.setAdPic(picURL);
        ad.setAdPrice(Double.parseDouble(StringUtils.trimAllWhitespace(price)));
        ad.setAdStartTime(startTime);
        ad.setAdEndTime(endTime);
        adService.newAd(ad);
        return new JsonResult();
    }

    /**
     * 删除广告信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/delAd",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResult delAd(Integer id){
        adService.deleteByPrimaryKey(id);
        return new JsonResult();
    }

    /**
     *更新广告信息
     * @param userId
     * @param type
     * @param typeCode
     * @param title
     * @param html
     * @param text
     * @param picURL
     * @param price
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/updateAd",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResult updateAd(Integer userId,
                            String type,String typeCode,
                            String title,String html,
                            String text,String picURL,
                            String price,
                            Date startTime,Date endTime){
        Ad ad = new Ad();
        ad.setAdUserId(userId);
        ad.setAdType(type);
        ad.setAdTypeCode(typeCode);
        ad.setAdTitle(title);
        ad.setAdHtml(html);
        ad.setAdText(text);
        ad.setAdPic(picURL);
        ad.setAdPrice(Double.parseDouble(StringUtils.trimAllWhitespace(price)));
        ad.setAdStartTime(startTime);
        ad.setAdEndTime(endTime);
        adService.updateByPrimaryKeySelective(ad);
        return new JsonResult();
    }

    /**
     *  根据id查找广告信息的详细信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectAdDetail",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResult selectAd(Integer id){
        Ad ad = adService.selectByPrimaryKey(id);
        return new JsonResult(ad);
    }
}
