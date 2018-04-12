package com.jsut.zachweb.service.impl;

import com.jsut.zachweb.dao.UserMapper;
import com.jsut.zachweb.model.User;
import com.jsut.zachweb.service.UserService;
import com.jsut.zachweb.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    //日志记录，存到log目录下的sys.log文件中
    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String NO_AUTHENTICATE ="NO_AUTHENTICATE";
    private static final String HAS_AUTHENTICATED ="HAS_AUTHENTICATED";
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
        User userSelectByUserName = userMapper.selectByUserName(user.getUserName());
        if (null!=userSelectByUserName){
            log.info("用户名已存在！");
            throw new ServiceException("该昵称已被注册，换个试试吧！~");
        }
        user.setUserPassword(MD5.getInstance().getMD5(user.getUserPassword()));
        user.setUserRegistTime(DateUtil.getNowDate());
        user.setUserVipGrade("1");
        user.setUserMoney(10);
        user.setUserTypeCode(getUserTypeCode(user.getUserType()));
        log.info(">>>>>>user.getUserType"+user.getUserType());
        log.info(">>>>>>user.getUserTypeCode"+user.getUserTypeCode());
        user.setUserVerifyStatus(this.getUserVerifyStatus(user.getUserType()));
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

    @Override
    public String sendCode(String email) {
        if (!RegExpValidator.isEmail(email)){
            log.info("邮箱格式不正确！");
            throw new ServiceException("邮箱格式不正确！请重新填写");
        }else {
            UUID uuid = UUID.randomUUID();
            String code = uuid.toString().substring(0,4);
            String subject = "ZachWeb 广告信息网修改密码验证码";
            String content = "您的验证码是："+code;
            mailService.sendSimpleMail(email,subject,content);
            return code;
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

    /**
     * 用户类型（普通用户/政府/组织/个体户/合伙企业/有限责任公司/股份有限公司/外资公司）
     * 用户类型编码（USER/GOV/ORG/SPB/COR/COLTD/LTD/FORCO）
     * @param userType
     * @return
     */
    public String getUserTypeCode(String userType){
        String userTypeCode;
        if (StringUtils.isEmpty(userType)||null==userType){
            log.info("用户类型为空！");
            throw new ServiceException("用户类型不允许为空！");
        }else{
            if (userType.equalsIgnoreCase("普通用户")){
                userTypeCode="USER";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase("政府")){
                userTypeCode="GOV";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase("组织")){
                userTypeCode="ORG";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase("个体户")){
                userTypeCode="SPB";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase("合伙企业")){
                userTypeCode="COR";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase("有限责任公司")){
                userTypeCode="COLTD";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase("股份有限公司")){
                userTypeCode="LTD";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase("外资公司")){
                userTypeCode="FORCO";
                return userTypeCode;
            }else{
                return "OTHER";
            }

        }

    }

    public String getUserVerifyStatus(String userType){
        String userVerifyStatus = "";
        if (StringUtils.isEmpty(userType)||null==userType){
            log.info("用户类型为空！");
            throw new ServiceException("用户类型不允许为空！");
        }else{
            if (userType.equalsIgnoreCase("普通用户")){
                userVerifyStatus=HAS_AUTHENTICATED;
            }else{
                userVerifyStatus=NO_AUTHENTICATE;
            }

        }
        return userVerifyStatus;
    }
}
