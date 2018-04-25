package com.jsut.zachweb.service;

import com.jsut.zachweb.model.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Integer userId,Integer adId,String content);

    List<Comment> findComments(Integer adId);

    void delComments(Integer id);
}
