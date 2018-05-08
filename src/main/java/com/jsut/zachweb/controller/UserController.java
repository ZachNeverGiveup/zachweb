package com.jsut.zachweb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jsut.zachweb.dto.AdminUserResponseDTO;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            User user =userService.findUser(((User) object).getUserId());
            log.info(user.toString());
            return new JsonResult(user);
        }else {
            log.info("未找到User");
            return new JsonResult("user404");
        }
    }

    /**
     * 登出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/logout")
    @ResponseBody
    public JsonResult logOut(HttpServletRequest request,HttpServletResponse response){
        log.info("logout>>>");
        log.info("request>>>"+request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        try {
            request.getSession().removeAttribute("user");
            return new JsonResult();
        }catch(Exception e){
            log.info("删除session失败");
            throw new ServiceException("删除session失败");
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
     * 忘记密码-->修改密码
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
     * 修改密码
     * @param oldPassword,newPassword
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/updatePassword")
    @ResponseBody
    public JsonResult updatePassword(String oldPassword,String newPassword,HttpServletRequest request,HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        User user= (User) request.getSession().getAttribute("user");
        userService.updatePassword(user,oldPassword,newPassword);
        try {
            request.getSession().removeAttribute("user");
            return new JsonResult();
        }catch(Exception e){
            log.info("删除session失败");
            throw new ServiceException("删除session失败");
        }
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

    @RequestMapping(value = "/recharge")
    @ResponseBody
    public JsonResult recharge(String money,HttpServletRequest request,HttpServletResponse response){
        log.info(">>>>>>余额充值:money:{}",money);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        User user = (User) request.getSession().getAttribute("user");
        userService.recharge(money,user);
        return new JsonResult();
    }

    /**
     * 用户申请认证
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "toVerify")
    @ResponseBody
    public JsonResult toVerify(HttpServletRequest request,HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        User user = (User) request.getSession().getAttribute("user");
        log.info(">>>>>>用户  {}  申请认证",user.getUserName());
        userService.toVerify(user);
        return new JsonResult();
    }

    /**
     * 管理员查找所有用户
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/admin/findUser")
    @ResponseBody
    public AdminUserResponseDTO adminFindUser(String page,String limit,HttpServletRequest request,HttpServletResponse response){
        log.info(">>>>>>>>管理员查找所有用户>>>>>>>>>>>>>>{},{}",page,limit);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        /*User user = (User) request.getSession().getAttribute("user");
        if (user.getUserGrade()<2){
            throw new ServiceException("你没有权限");
        }*/
        List<User> users = userService.findUserByPage(page,limit);
        AdminUserResponseDTO adminUserResponseDTO = new AdminUserResponseDTO();
        adminUserResponseDTO.setCode(0);
        adminUserResponseDTO.setMsg("");
        List<User> allUser = userService.findAllUser();
        adminUserResponseDTO.setCount(allUser.size());
        adminUserResponseDTO.setData(users);
        return adminUserResponseDTO;
    }
    /**
     * 管理员查找所有待认证的用户
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/admin/findPendingUser")
    @ResponseBody
    public AdminUserResponseDTO findPendingUser(String page,String limit,HttpServletRequest request,HttpServletResponse response){
        log.info(">>>>>>>>管理员查找所有待认证的用户>>>>>>>>>>>>>>{},{}",page,limit);
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        /*User user = (User) request.getSession().getAttribute("user");
        if (user.getUserGrade()<2){
            throw new ServiceException("你没有权限");
        }*/
        List<User> users = userService.findUserPendingByPage(page,limit);
        AdminUserResponseDTO adminUserResponseDTO = new AdminUserResponseDTO();
        adminUserResponseDTO.setCode(0);
        adminUserResponseDTO.setMsg("");
        List<User> allUser = userService.findAllPendingUser();
        adminUserResponseDTO.setCount(allUser.size());
        adminUserResponseDTO.setData(users);
        return adminUserResponseDTO;
    }

    /**
     * 通过认证
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/giveAuthenticated")
    @ResponseBody
    public JsonResult giveAuthenticated(String id,HttpServletRequest request,HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        User user = (User) request.getSession().getAttribute("user");
        if (user.getUserGrade()<2){
            throw new ServiceException("你没有权限");
        }
        Integer userId = Integer.parseInt(id);
        userService.giveAuthenticated(userId);
        return new JsonResult();
    }

    /**
     * 删除用户
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/delUser")
    @ResponseBody
    public JsonResult toVerify(String id,HttpServletRequest request,HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        User user = (User) request.getSession().getAttribute("user");
        if (user.getUserGrade()<2){
            throw new ServiceException("你没有权限");
        }
        log.info(">>>>>>要删除的用户id:{}",id);
        Integer userId = Integer.parseInt(id);
        userService.delUser(userId);
        return new JsonResult();
    }

    /**
     * 赋予发表广告的权限
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/givePermission")
    @ResponseBody
    public JsonResult givePermission(String id,HttpServletRequest request,HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        User user = (User) request.getSession().getAttribute("user");
        if (user.getUserGrade()<2){
            throw new ServiceException("你没有权限");
        }
        Integer userId = Integer.parseInt(id);
        userService.givePermission(userId);
        return new JsonResult();
    }
    /**
     * 撤回发表广告的权限
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/delPermission")
    @ResponseBody
    public JsonResult delPermission(String id,HttpServletRequest request,HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        User user = (User) request.getSession().getAttribute("user");
        if (user.getUserGrade()<2){
            throw new ServiceException("你没有权限");
        }
        Integer userId = Integer.parseInt(id);
        userService.delPermission(userId);
        return new JsonResult();
    }

    /**
     * 上传头像
     * @return json字符串
     */
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public String uploadFile(MultipartFile file,HttpServletRequest request,HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin").toString());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        if(file.isEmpty()){
            return "false";
        }
        String fileName = file.getOriginalFilename();

        String path = System.getProperty("user.dir") + "/uploadFile" ;
        File dest = new File(path + "/" + fileName);
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            return "true";
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        }
        /*// 返回结果用 json对象
        JSONObject returnObj = new JSONObject();
        //从请求中获取请求的json字符串
        String strData = req.getParameter("data");
        //将获取到的JSON字符串转换为Imgidx对象
        //UploadInfo info = JSON.parseObject(strData, UploadInfo.class);
        log.info(">>>>>>>>>strData>>>>>>>>>>"+strData);
        //获取上传的文件集合
        List<MultipartFile> files = ((MultipartHttpServletRequest)req).getFiles("file");
        MultipartFile file = files.get(0);
        // 返回信息头部
        Map<String, String> header = new HashMap<String, String>();
        header.put("code", "0");
        header.put("msg", "success");
        File file1234 = new File(file.getOriginalFilename());
        //插入数据的影响的数据条数
        int result = 0;
        //将文件上传到save
        if(!file.isEmpty()){
            try{
                byte[] arr = new byte[1024];
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file1234));
                bos.write(arr);
                bos.flush();
                bos.close();
                header.put("code", "1");
                header.put("msg", "success");
                header.put("url", file1234.getAbsolutePath());
            }catch(Exception e){
                header.put("code", "-1");
                header.put("msg", "errorMsg:" + e.getMessage());
            }
        }else{
            header.put("code", "-1");
            header.put("msg", "errorMsg:上传文件失败，因为文件是空的");
        }
        //String returnStr = returnObj.toJSONString(header);
        return header;*/
    }
}
