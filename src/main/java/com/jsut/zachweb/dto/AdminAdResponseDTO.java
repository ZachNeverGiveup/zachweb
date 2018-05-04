package com.jsut.zachweb.dto;

import com.jsut.zachweb.model.Ad;

import java.util.List;

public class AdminAdResponseDTO {
    private Integer code;
    private String msg;
    private Integer count;
    private List<Ad> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Ad> getData() {
        return data;
    }

    public void setData(List<Ad> data) {
        this.data = data;
    }
}
