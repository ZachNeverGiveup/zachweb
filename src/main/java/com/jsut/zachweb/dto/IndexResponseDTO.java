package com.jsut.zachweb.dto;

import com.jsut.zachweb.model.Ad;

import java.util.List;

public class IndexResponseDTO {
    private List<Ad> adLikeList;
    private List<Ad> indexAdList;
    private List<Ad> indexAdListComment;
    private List<Ad> indexAdListClick;

    public List<Ad> getAdLikeList() {
        return adLikeList;
    }

    public void setAdLikeList(List<Ad> adLikeList) {
        this.adLikeList = adLikeList;
    }

    public List<Ad> getIndexAdList() {
        return indexAdList;
    }

    public void setIndexAdList(List<Ad> indexAdList) {
        this.indexAdList = indexAdList;
    }

    public List<Ad> getIndexAdListComment() {
        return indexAdListComment;
    }

    public void setIndexAdListComment(List<Ad> indexAdListComment) {
        this.indexAdListComment = indexAdListComment;
    }

    public List<Ad> getIndexAdListClick() {
        return indexAdListClick;
    }

    public void setIndexAdListClick(List<Ad> indexAdListClick) {
        this.indexAdListClick = indexAdListClick;
    }

    @Override
    public String toString() {
        return "IndexResponseDTO{" +
                "adLikeList=" + adLikeList +
                ", indexAdList=" + indexAdList +
                ", indexAdListComment=" + indexAdListComment +
                ", indexAdListClick=" + indexAdListClick +
                '}';
    }
}
