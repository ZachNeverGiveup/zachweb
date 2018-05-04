package com.jsut.zachweb.service;

import com.jsut.zachweb.model.User;

import java.text.ParseException;
import java.util.List;

public interface UserService {

    void regist(User user) throws ParseException;

    User login(User user);

    String sendCode(String email);

    User resetPassword(String userEmail,String userPassword);

    void updateUser(User user);

    User findUser(Integer id);

    void payForAd(Integer money,Integer userId);

    void recharge(String money, User user);

    void toVerify(User user);

    void updatePassword(User user, String oldPassword, String newPassword);

    List<User> findAllUser();

    List<User> findUserByPage(String page,String limit);

    void delUser(Integer userId);

    void givePermission(Integer userId);

    void delPermission(Integer userId);

    List<User> findUserPendingByPage(String page, String limit);

    List<User> findAllPendingUser();

    void giveAuthenticated(Integer userId);
}
