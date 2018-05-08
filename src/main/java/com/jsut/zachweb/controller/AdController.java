package com.jsut.zachweb.controller;

import com.jsut.zachweb.dto.AdCollectDTO;
import com.jsut.zachweb.dto.AdminAdResponseDTO;
import com.jsut.zachweb.dto.IndexResponseDTO;
import com.jsut.zachweb.model.Ad;
import com.jsut.zachweb.model.User;
import com.jsut.zachweb.service.AdService;
import com.jsut.zachweb.service.UserService;
import com.jsut.zachweb.util.DateUtil;
import com.jsut.zachweb.util.JsonResult;
import com.jsut.zachweb.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
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

    @Autowired
    private UserService userService;

    /**
     * 新增广告信息
     *
     * @param type
     * @param title
     * @param html
     * @param text
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/newAd", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResult newAd(String type, String title,
                            String html, String text,
            /* String picURL, String price,*/
                            String startTime, String endTime, String money,
                            HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        log.info(">>>type:" + type);
        log.info(">>>title:" + title);
        log.info(">>>html:" + html);
        log.info(">>>text:" + text);
        /*log.info(">>>pic:"+picURL);
        log.info(">>>price:"+price);*/
        log.info(">>>startTime:" + startTime);
        log.info(">>>endTime:" + endTime);
        User user = (User) request.getSession().getAttribute("user");
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

        userService.payForAd(Integer.parseInt(StringUtils.trimAllWhitespace(money)), user.getUserId());
        return new JsonResult();
    }

    /**
     * 删除广告信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delAd", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResult delAd(Integer id, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        User user = (User) request.getSession().getAttribute("user");
        adService.deleteByPrimaryKey(id,user);
        return new JsonResult();
    }

    /**
     * 更新广告信息
     *
     * @param type
     * @param title
     * @param html
     * @param text
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/updateAd", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResult updateAd(String id, String type,
                               String title, String html,
                               String text,
                               String price,
                               String startTime, String endTime, String money, HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Ad ad = new Ad();
        ad.setAdId(Integer.parseInt(id));
        ad.setAdUserId(((User) request.getSession().getAttribute("user")).getUserId());
        ad.setAdType(type);
        ad.setAdTitle(title);
        ad.setAdHtml(html);
        ad.setAdText(text);
        //ad.setAdPic(picURL);
        ad.setAdPrice(Double.parseDouble(StringUtils.trimAllWhitespace(price)));
        ad.setAdStartTime(DateUtil.DateFormat(startTime));
        ad.setAdEndTime(DateUtil.DateFormat(endTime));
        log.info("要修改的广告信息是：" + ad.toString());
        adService.updateByPrimaryKeySelective(ad);
        return new JsonResult();
    }

    /**
     * 根据id查找广告信息的详细信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectAdDetail", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResult selectAd(Integer id, HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Ad ad = adService.selectByPrimaryKey(id);
        return new JsonResult(ad);
    }

    /**
     * 查找用户发表的广告
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/findUserAd")
    @ResponseBody
    public JsonResult findUser(HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>>进入finduserad");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Object object = request.getSession().getAttribute("user");
        if (null != object && object instanceof User) {
            User user = (User) object;
            List<Ad> ads = adService.selectAdByUserId(user.getUserId());
            if (!CollectionUtils.isEmpty(ads)) {
                return new JsonResult(ads);
            } else {
                log.info("该用户未发表广告");
                JsonResult jsonResult = new JsonResult();
                jsonResult.setState(0);
                return jsonResult;
            }
        } else {
            log.info("未找到User");
            return new JsonResult("未找到User");
        }
    }

    /**
     * 查找用户收藏的广告
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/findUserCollect")
    @ResponseBody
    public JsonResult findUserCollect(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        User user = (User)request.getSession().getAttribute("user");
        List<AdCollectDTO> ads = adService.findUserCollect(user.getUserId());
        return new JsonResult(ads);
    }

    @RequestMapping(value = "isCollectAd")
    @ResponseBody
    public JsonResult isCollectAd(Integer id, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>用户是否收藏广告");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Object object = request.getSession().getAttribute("user");
        if (null != object && object instanceof User) {
            User user = (User) object;
            boolean flag = adService.iscollectAd(id, user);
            if (flag) {
                log.info(">>>>用户是否收藏该广告：{}", flag);
                return new JsonResult("true");
            } else {
                log.info(">>>>用户是否收藏该广告：{}", flag);
                return new JsonResult("false");
            }

        }
        log.info("用户为空！");
        throw new ServiceException("请先登录！");
    }

    /**
     * 用户收藏广告
     *
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/collectAd")
    @ResponseBody
    public JsonResult collectAd(Integer id, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>用户收藏广告");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Object object = request.getSession().getAttribute("user");
        if (null != object && object instanceof User) {
            User user = (User) object;
            adService.collectAd(id, user);
            return new JsonResult();
        }
        log.info("用户为空！");
        throw new ServiceException("请先登录！");
    }

    /**
     * 用户取消收藏广告
     *
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/cancelCollectAd")
    @ResponseBody
    public JsonResult cancelCollectAd(Integer id, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>用户取消收藏广告");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Object object = request.getSession().getAttribute("user");
        if (null != object && object instanceof User) {
            User user = (User) object;
            adService.canceCollectAd(id, user);
            return new JsonResult();
        }
        log.info("用户为空！");
        throw new ServiceException("请先登录！");
    }

    /**
     * 根据关键词搜索广告
     *
     * @param keyword
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/searchAds")
    @ResponseBody
    public JsonResult searchAds(String keyword, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>根据关键词： {} 搜索>>>", keyword);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        List<Ad> adList = adService.selectAdsByKeyWord(keyword);
        List<Ad> adClickListByPage = adService.findIndexAdListByPage(null, "a.ad_click_number", "1", "12");
        IndexResponseDTO indexResponseDTO = new IndexResponseDTO();
        indexResponseDTO.setIndexAdList(adList);
        indexResponseDTO.setIndexAdListClick(adClickListByPage);
        return new JsonResult(indexResponseDTO);
    }

    /**
     * 管理员查找所有用户
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/admin/findAds")
    @ResponseBody
    public AdminAdResponseDTO adminFindAd(String page, String limit, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>>>管理员查找所有广告>>>>>>>>>>>>>>{},{}", page, limit);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        AdminAdResponseDTO adminAdResponseDTO = new AdminAdResponseDTO();
        adminAdResponseDTO.setCode(0);
        adminAdResponseDTO.setMsg("success find all ads");
        List<Ad> adList = adService.findAllAds();
        adminAdResponseDTO.setCount(adList.size());
        List<Ad> adListByPage = adService.findAdsByPage(page,limit);
        adminAdResponseDTO.setData(adListByPage);
        return adminAdResponseDTO;
    }

    /**
     * 首页信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/indexAd")
    @ResponseBody
    public JsonResult indexAd(HttpServletRequest request, HttpServletResponse response){
        log.info(">>>>>>>>进入首页>>>>>>>>>>>>>>");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        User user = (User)request.getSession().getAttribute("user");
        List<Ad> adLikeList = adService.findUserLikeAdList(user);
        List<Ad> indexAdListByPage = adService.findIndexAdListByPage(null, null, "1", "12");
        List<Ad> indexAdCommentListByPage = adService.findIndexAdListByPage(null, "a.ad_comment_number", "1", "12");
        List<Ad> indexAdClickListByPage = adService.findIndexAdListByPage(null, "a.ad_click_number", "1", "12");
        IndexResponseDTO indexResponseDTO = new IndexResponseDTO();
        indexResponseDTO.setAdLikeList(adLikeList);
        indexResponseDTO.setIndexAdList(indexAdListByPage);
        indexResponseDTO.setIndexAdListComment(indexAdCommentListByPage);
        indexResponseDTO.setIndexAdListClick(indexAdClickListByPage);
        return new JsonResult(indexResponseDTO);
    }

    /**
     * jie分页分每页条数分类别分排序方式查看信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/selectAdList")
    @ResponseBody
    public JsonResult selectAdList(String adType,String sortType,String page, String limit, HttpServletRequest request, HttpServletResponse response){
        log.info(">>>>>>>>进入首页>>>>>>>>>>>>>>");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        List<Ad> AdListByPage = adService.findIndexAdListByPage(adType, sortType, page, limit);
        List<Ad> adClickListByPage = adService.findIndexAdListByPage(adType, "a.ad_click_number", "1", "12");
        IndexResponseDTO indexResponseDTO = new IndexResponseDTO();
        indexResponseDTO.setIndexAdList(AdListByPage);
        indexResponseDTO.setIndexAdListClick(adClickListByPage);
        return new JsonResult(indexResponseDTO);
    }

}
