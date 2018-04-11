package com.jsut.zachweb.controller;

import com.jsut.zachweb.model.User;
import com.jsut.zachweb.service.UserService;
import com.jsut.zachweb.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public JsonResult Login(String userName,String userPassword,HttpServletRequest request,HttpSession session,HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        /*response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "5000");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, If-Modified-Since, " +
                        "Pragma, Last-Modified, Cache-Control, Expires, " +
                        "Content-Type, X-E4M-With,Authorization,Token");*/
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
       /* response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "5000");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, " +
                "If-Modified-Since, Pragma, Last-Modified, " +
                        "Cache-Control, Expires, Content-Type, " +
                        "X-E4M-With,Authorization,Token");*/
        HttpSession session =request.getSession();
        User user = (User)session.getAttribute("user");
        if(null!=user){
            log.info(user.toString());
            return new JsonResult(user);
        }else {
            log.info("未找到User");
            return new JsonResult("未找到User");
        }
    }
}
