package com.jsut.zachweb.controller;

import com.jsut.zachweb.model.Ad;
import com.jsut.zachweb.model.User;
import com.jsut.zachweb.service.AdService;
import com.jsut.zachweb.util.DateUtil;
import com.jsut.zachweb.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/ad")
public class AdController {

    //日志记录，存到log目录下的sys.log文件中
    private static Logger log = LoggerFactory.getLogger(AdController.class);

    @Autowired
    private AdService adService;

    /**
     * 新增广告信息
     * @param type
     * @param title
     * @param html
     * @param text
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/newAd",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResult newAd(String type,String title,
                            String html,String text,
                           /* String picURL, String price,*/
                            String startTime, String endTime,
            HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        log.info(">>>type:"+type);
        log.info(">>>title:"+title);
        log.info(">>>html:"+html);
        log.info(">>>text:"+text);
        /*log.info(">>>pic:"+picURL);
        log.info(">>>price:"+price);*/
        log.info(">>>startTime:"+startTime);
        log.info(">>>endTime:"+endTime);
        User user = (User)request.getSession().getAttribute("user");
        Ad ad = new Ad();
        ad.setAdUserId(user.getUserId());
        ad.setAdType(type);
        ad.setAdTitle(title);
        ad.setAdHtml(html);
        ad.setAdText(text);
     /*   ad.setAdPic(picURL);
        ad.setAdPrice(Double.parseDouble(StringUtils.trimAllWhitespace(price)));*/
        ad.setAdStartTime(DateUtil.DateFormat(startTime));
        ad.setAdEndTime(DateUtil.DateFormat(endTime));
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

    /**
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/findUserAd")
    @ResponseBody
    public JsonResult findUser(HttpServletRequest request,HttpServletResponse response){
        log.info(">>>>>>>进入finduserad");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Object object = request.getSession().getAttribute("user");
        if(null!=object&&object instanceof User){
            User user = (User)object;
            List<Ad> ads = adService.selectAdByUserId(user.getUserId());
            return new JsonResult(ads);
        }else {
            log.info("未找到User");
            return new JsonResult("未找到User");
        }
    }
}
