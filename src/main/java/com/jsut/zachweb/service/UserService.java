package com.jsut.zachweb.service;

import com.jsut.zachweb.model.User;

import java.text.ParseException;

public interface UserService {

    void regist(User user) throws ParseException;

    User login(User user);

    String sendCode(String email);
}
