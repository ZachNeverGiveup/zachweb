package com.jsut.zachweb.controller;

import com.jsut.zachweb.model.User;
import com.jsut.zachweb.service.UserService;
import com.jsut.zachweb.util.JsonResult;
import com.jsut.zachweb.util.MD5;
import com.jsut.zachweb.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;

/**
 * 用户控制层
 */
@Controller
public class UserController {

    //日志记录，存到log目录下的sys.log文件中
    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    /**
     * 注册controller
     * @return
     */
    @RequestMapping(value="/regist",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResult Regist(String userName, String userPassword,
                             String userType,String userPhone,
                             String userEmail,String userSex,
                             String userCity,String userSignature,
                             HttpServletResponse response) throws ParseException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        User user = new User();
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setUserType(userType);
        user.setUserPhone(userPhone);
        user.setUserEmail(userEmail);
        user.setUserSex(userSex);
        user.setUserCity(userCity);
        user.setUserSignature(userSignature);
        userService.regist(user);
        JsonResult jsonResult = new JsonResult(user);
        return jsonResult;
    }

    /**
     * 登录
     * @param
     * @param response
     * @return
     */
    @RequestMapping(value="/login",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public JsonResult Login(String userName,String userPassword,HttpServletRequest request,HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        User user = new User();
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        log.info(user.toString());
        User user1 = userService.login(user);
        if (null!=user1){
            JsonResult jsonResult = new JsonResult(user1);
            request.getSession().setAttribute("user",user1);
            log.info(user1.toString());
            return jsonResult;
        }else{
            log.info("登陆失败");
            return new JsonResult("登陆失败");
        }
    }

    /**
     * 从session中查找user信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/findUser")
    @ResponseBody
    public JsonResult findUser(HttpServletRequest request,HttpServletResponse response){
        log.info("进入findUser");
        log.info("request>>>"+request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        Object object = request.getSession().getAttribute("user");
        if(null!=object&&object instanceof User){
            User user = (User)object;
            log.info(user.toString());
            return new JsonResult(user);
        }else {
            log.info("未找到User");
            return new JsonResult("未找到User");
        }
    }

    /**
     * 发送验证码
     * @param email
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/sendCode")
    @ResponseBody
    public JsonResult sendCode(String email,HttpServletRequest request,HttpServletResponse response){
        log.info("sendCode");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        String code = userService.sendCode(email);
        if (!StringUtils.isEmpty(code)){
            request.getSession().setAttribute("code",code);
        }
        return new JsonResult();
    }

    /**
     *
     * @param code
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/verifyCode")
    @ResponseBody
    public JsonResult verifyCode(String code,HttpServletRequest request,HttpServletResponse response){
        log.info(">>>>>>验证验证码是否正确:code,{}",code);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        String codeSession = request.getSession().getAttribute("code").toString();
        if (StringUtils.isEmpty(codeSession)){
            return new JsonResult("session里面没有code！");
        }else{
            if (codeSession.equalsIgnoreCase(code)){
                log.info(">>>>>>验证成功！！！");
                return new JsonResult();
            }else{
                throw new ServiceException("验证码错误");
            }
        }
    }

    /**
     * 修改密码
     * @param userPassword
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/resetPassword")
    @ResponseBody
    public JsonResult resetPassword(String userEmail,String userPassword,HttpServletRequest request,HttpServletResponse response){
        log.info(">>>>>>重置新密码:userPassword:{},userEmail:{}",userPassword,userEmail);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        User user = userService.resetPassword(userEmail, userPassword);
        return new JsonResult(user);
    }

    /**
     * 修改个人信息
     * @param userName
     * @param userEmail
     * @param userCity
     * @param userSignature
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/updateUserInfo")
    @ResponseBody
    public JsonResult updateUserInfo(String userName,String userEmail,String userCity,String userSignature,HttpServletRequest request,HttpServletResponse response){
        log.info(">>>>>>修改个人信息:userName:{},userEmail:{}",userName,userEmail);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        User user = (User) request.getSession().getAttribute("user");
        user.setUserName(userName);
        user.setUserEmail(userEmail);
        user.setUserCity(userCity);
        user.setUserSignature(userSignature);
        userService.updateUser(user);
        return new JsonResult(user);
    }
}
