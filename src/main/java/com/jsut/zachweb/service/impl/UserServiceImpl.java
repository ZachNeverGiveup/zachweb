package com.jsut.zachweb.service.impl;

import com.jsut.zachweb.constants.ZachWebConstants;
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
import java.util.List;
import java.util.UUID;

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
        User userSelectByUserName = userMapper.selectByUserName(user.getUserName());
        if (null!=userSelectByUserName){
            log.info("用户名已存在！");
            throw new ServiceException("该昵称已被注册，换个试试吧！~");
        }
        User userSelectByUserEmail = userMapper.selectByEmail(user.getUserEmail());
        if (null!=userSelectByUserEmail){
            log.info("邮箱已存在！");
            throw new ServiceException("该邮箱已被注册，换个试试吧！~");
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

    @Override
    public User resetPassword(String userEmail,String userPassword) {
        User userSelectByEmail = userMapper.selectByEmail(userEmail);
        if (null!=userSelectByEmail){
            userSelectByEmail.setUserPassword(MD5.getInstance().getMD5(userPassword));
            userMapper.updateByPrimaryKeySelective(userSelectByEmail);
        }
        String subject = "ZachWeb 广告信息网修改密码提醒";
        String content = "你刚刚修改了密码，如不是本人操作，请联系管理员";
        mailService.sendSimpleMail(userEmail,subject,content);
        return userSelectByEmail;
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User findUser(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void payForAd(Integer money,Integer userId) {
        log.info("money>>>{}",money);
        log.info("userId>>>{}",userId);
        User user = userMapper.selectByPrimaryKey(userId);
        if (null==user){
            throw new ServiceException("user对象为空！~");
        }
        Integer leftMoney = user.getUserMoney()-money;
        log.info("用户{}消费了{}，余额是{}",user.getUserName(),money,leftMoney);
        user.setUserMoney(leftMoney);
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 用户充值
     * @param money
     * @param user
     */
    @Override
    public void recharge(String money, User user) {
        if (null==user){
            throw new ServiceException("user对象为空！~");
        }else{
            try{
                Integer rechargeMoney = Integer.parseInt(StringUtils.trimAllWhitespace(money));
                Integer newMoney = user.getUserMoney()+rechargeMoney;
                user.setUserMoney(newMoney);
                userMapper.updateByPrimaryKeySelective(user);
            }catch (Exception e){
                throw new ServiceException("请输入正确的数字！");
            }
        }
    }

    /**
     * 申请认证
     * @param user
     */
    @Override
    public void toVerify(User user) {
        User user1 = userMapper.selectByPrimaryKey(user.getUserId());
        if (null==user1){
            throw new ServiceException("user对象为空！~");
        }
        user1.setUserVerifyStatus(ZachWebConstants.PENDING);
        userMapper.updateByPrimaryKeySelective(user1);
    }

    /**
     * 更新密码
     * @param user
     * @param oldPassword
     * @param newPassword
     */
    @Override
    public void updatePassword(User user, String oldPassword, String newPassword) {
        log.info(">>>>>>>>oldPassword>>>>>"+oldPassword);
        User user1 = userMapper.selectByPrimaryKey(user.getUserId());
        log.info(">>>>>>>>MD5.getInstance().getMD5(oldPassword)>>>>>"+MD5.getInstance().getMD5(oldPassword));
        log.info(">>>>>>>>user1.getUserPassword()>>>>>"+user1.getUserPassword());
        if(null!=user1){
            if (!MD5.getInstance().getMD5(oldPassword).equalsIgnoreCase(user1.getUserPassword())){
                throw new ServiceException("原密码错误！！！");
            }else{
                user1.setUserPassword(MD5.getInstance().getMD5(newPassword));
                userMapper.updateByPrimaryKeySelective(user1);
                String subject = "ZachWeb 广告信息网修改密码提醒";
                String content = "你刚刚修改了密码，如不是本人操作，请联系管理员";
                mailService.sendSimpleMail(user1.getUserEmail(),subject,content);
            }

        }
    }

    @Override
    public List<User> findAllUser() {
        List<User> users = userMapper.selectAllUser();
        return users;
    }

    @Override
    public List<User> findUserByPage(String page, String limit) {
        Integer pageSize = Integer.parseInt(limit);
        Integer pageStart = pageSize * (Integer.parseInt(page) - 1);
        List<User> users = userMapper.findUserByPage(pageStart,pageSize);
        return users;
    }

    /**
     * 删除用户
     * @param userId
     */
    @Override
    public void delUser(Integer userId) {
        userMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public void givePermission(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(null==user){
            throw new ServiceException("该用户不存在！");
        }
        user.setUserGrade(1);
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void delPermission(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(null==user){
            throw new ServiceException("该用户不存在！");
        }
        user.setUserGrade(0);
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<User> findUserPendingByPage(String page, String limit) {
        Integer pageSize = Integer.parseInt(limit);
        Integer pageStart = pageSize * (Integer.parseInt(page) - 1);
        List<User> users = userMapper.findPendingUserByPage(pageStart,pageSize);
        return users;

    }

    @Override
    public List<User> findAllPendingUser() {
        List<User> users = userMapper.selectPendingUser();
        return users;
    }

    @Override
    public void giveAuthenticated(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(null==user){
            throw new ServiceException("该用户不存在！");
        }
        user.setUserVerifyStatus(ZachWebConstants.HAS_AUTHENTICATED);
        userMapper.updateByPrimaryKeySelective(user);
    }


    /**
     * 用户参数验证
     * @param user
     */
    private void validateUser(User user){
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

    private void sendRegistMail(String to){
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
    private String getUserTypeCode(String userType){
        String userTypeCode;
        if (StringUtils.isEmpty(userType)||null==userType){
            log.info("用户类型为空！");
            throw new ServiceException("用户类型不允许为空！");
        }else{
            if (userType.equalsIgnoreCase(ZachWebConstants.USER)){
                userTypeCode="USER";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase(ZachWebConstants.GOV)){
                userTypeCode="GOV";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase(ZachWebConstants.ORG)){
                userTypeCode="ORG";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase(ZachWebConstants.SPB)){
                userTypeCode="SPB";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase(ZachWebConstants.COR)){
                userTypeCode="COR";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase(ZachWebConstants.COLTD)){
                userTypeCode="COLTD";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase(ZachWebConstants.LTD)){
                userTypeCode="LTD";
                return userTypeCode;
            }else if (userType.equalsIgnoreCase(ZachWebConstants.FORCO)){
                userTypeCode="FORCO";
                return userTypeCode;
            }else{
                return "OTHER";
            }

        }

    }

    private String getUserVerifyStatus(String userType){
        String userVerifyStatus = "";
        if (StringUtils.isEmpty(userType)){
            log.info("用户类型为空！");
            throw new ServiceException("用户类型不允许为空！");
        }else{
            if (userType.equalsIgnoreCase("普通用户")){
                userVerifyStatus=ZachWebConstants.HAS_AUTHENTICATED;
            }else{
                userVerifyStatus=ZachWebConstants.NO_AUTHENTICATE;
            }
        }
        return userVerifyStatus;
    }
}
