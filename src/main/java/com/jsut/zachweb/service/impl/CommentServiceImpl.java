package com.jsut.zachweb.service.impl;

import com.jsut.zachweb.constants.ZachWebConstants;
import com.jsut.zachweb.dao.AdMapper;
import com.jsut.zachweb.dao.CommentMapper;
import com.jsut.zachweb.model.Ad;
import com.jsut.zachweb.model.Comment;
import com.jsut.zachweb.service.CommentService;
import com.jsut.zachweb.util.DateUtil;
import com.jsut.zachweb.util.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger LOG = Logger.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private AdMapper adMapper;


    @Override
    public void addComment(Integer userId, Integer adId, String content) {
        if (null==userId){
            throw new ServiceException("用户为空！");
        }
        if (null==adId){
            throw new ServiceException("广告为空！");
        }
        if (StringUtils.isEmpty(content)){
            throw new ServiceException("评论不能为空！");
        }
        if (content.length()> ZachWebConstants.COMMENT_MAX_LENGTH){
            throw new ServiceException("评论字数太长！");
        }
        try {
            Comment comment = new Comment();
            comment.setCommentAdId(adId);
            comment.setCommentUserId(userId);
            comment.setCommentContent(content);
            comment.setCommentTime(DateUtil.getNowDate());
            comment.setCommentLikeNumber(0);
            commentMapper.insertSelective(comment);
            //将该广告的评论数量加一
            Ad ad = adMapper.selectByPrimaryKey(adId);
            Integer adCommentNumber = ad.getAdCommentNumber()+1;
            ad.setAdCommentNumber(adCommentNumber);
            adMapper.updateByPrimaryKeySelective(ad);
        }catch(Exception e){
            throw new ServiceException("评论失败！失败原因："+e.getMessage());
        }
    }

    @Override
    public List<Comment> findComments(Integer adId) {
        if (null==adId){
            throw new ServiceException("广告id为空！");
        }
        return commentMapper.selectByAdId(adId);
    }

    @Override
    public void delComments(Integer id) {
        if (null==id){
            throw new ServiceException("评论id为空！删除失败");
        }
        commentMapper.deleteByPrimaryKey(id);
    }
}
