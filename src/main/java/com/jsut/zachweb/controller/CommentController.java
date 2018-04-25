package com.jsut.zachweb.controller;

import com.jsut.zachweb.dao.AdMapper;
import com.jsut.zachweb.model.Ad;
import com.jsut.zachweb.model.Comment;
import com.jsut.zachweb.model.User;
import com.jsut.zachweb.service.CommentService;
import com.jsut.zachweb.util.JsonResult;
import com.jsut.zachweb.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class CommentController {

    private static Logger log = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    /**
     * 提交评论
     * @param adId
     * @param content
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/commitComment")
    @ResponseBody
    public JsonResult commitComment(Integer adId, String content, HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Object object = request.getSession().getAttribute("user");
        if(null!=object&&object instanceof User) {
            User user = (User) object;
            commentService.addComment(user.getUserId(),adId,content);
            return new JsonResult();
        }
        log.info("用户为空！");
        throw new ServiceException("请先登录！");
    }

    @RequestMapping(value = "/findComments")
    @ResponseBody
    public JsonResult findComments(Integer adId, HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        List<Comment> comments = commentService.findComments(adId);
        return new JsonResult(comments);
    }

    @RequestMapping(value = "/delComment")
    @ResponseBody
    public JsonResult delComment(Integer id, HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        commentService.delComments(id);
        return new JsonResult();
    }
}
