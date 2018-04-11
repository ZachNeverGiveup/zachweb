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

    @RequestMapping(value = "/delAd",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResult delAd(Integer id){
        adService.deleteByPrimaryKey(id);
        return new JsonResult();
    }
}
