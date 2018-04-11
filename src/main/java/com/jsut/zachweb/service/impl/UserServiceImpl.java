package com.jsut.zachweb.service.impl;

import com.jsut.zachweb.dao.UserMapper;
import com.jsut.zachweb.model.User;
import com.jsut.zachweb.service.UserService;
import com.jsut.zachweb.util.MD5;
import com.jsut.zachweb.util.MailService;
import com.jsut.zachweb.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    //日志记录，存到log目录下的sys.log文件中
    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailService mailService;
    /**
     * 注册用户
     * @param user
     * @throws ParseException
     */
    @Override
    public void regist(User user) throws ParseException {
        validateUser(user);
        user.setUserPassword(MD5.getInstance().getMD5(user.getUserPassword()));
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = dFormat.format(new Date());
        user.setUserRegistTime(dFormat.parse(formatDate));
        userMapper.insertSelective(user);
        //发送邮件提醒
        sendRegistMail(user.getUserEmail());
    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        validateUser(user);
        user.setUserPassword(MD5.getInstance().getMD5(user.getUserPassword()));
        User userSelectByUserName = userMapper.selectByUserName(user.getUserName());
        if (null==userSelectByUserName){
            log.info("用户名不存在！");
            throw new ServiceException("用户名不存在！");
        }else{
            User userSelectByUserNameAndPwd = userMapper.selectByUserNameAndPwd(user);
            if (null==userSelectByUserNameAndPwd){
                log.info("密码错误！");
                throw new ServiceException("密码错误！");
            }else{
                log.info("验证成功！");
                return userSelectByUserNameAndPwd;
            }
        }
    }


    /**
     * 用户参数验证
     * @param user
     */
    public void validateUser(User user){
        if(null==user){
            throw new ServiceException("user对象为空！~");
        }
        if(StringUtils.isEmpty(user.getUserName())||user.getUserName()==null){
            throw new ServiceException("用户名为空！~");
        }
        if(StringUtils.isEmpty(user.getUserPassword())||user.getUserPassword()==null){
            throw new ServiceException("用户名密码为空！~");
        }
    }

    public void sendRegistMail(String to){
        String subject = "ZachWeb 广告信息网注册邮件提醒";
        String content = "欢迎注册新账号";
        mailService.sendSimpleMail(to,subject,content);
    }
}
