package com.example.textspringboot.Controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.example.textspringboot.Common.Result;
import com.example.textspringboot.Entity.user;
import com.example.textspringboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
public class UserController {
    @Resource
    UserMapper usermapper;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("/login")
    public Result<?> login(HttpServletRequest request, @RequestBody user userParam){
        System.out.println("login");
        user usertemp=usermapper.loginUser(userParam.getUserId());
//        System.out.println(usertemp);
        if(usertemp == null){
            return Result.error("-1","账号错误");
        }
        if (usertemp.getPassword().equals(userParam.getPassword())){
            HttpSession session=request.getSession();
            session.setAttribute("user",usertemp);
            return Result.success(usertemp);
        }else{
            return Result.error("-2","密码错误");
        }
    }
    @PostMapping("/logout")
    public Result<?> logout(){
        System.out.println("logout");
        request.getSession().invalidate();
        return Result.success();
    }
    @PostMapping("/register")
    public Result<?> register(@RequestBody user userParam){
        System.out.println("register");
        user usertemp=usermapper.loginUser(userParam.getUserId());
        if(usertemp != null){
            return Result.error("-1","用户已存在");
        }
        usermapper.UserRegister(userParam);
        String filePath="C:/Project/user/";
        String folderName = filePath+userParam.getUserId();
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return Result.success();
    }
    @PostMapping("/getmessage")
    public Result<?> getmessage(){
        System.out.println("getmessage");
        user u= (user) request.getSession().getAttribute("user");
        if(null==u){
            return Result.error("-1","Session过期");
        }else {
            return Result.success(u);
        }
    }
    @PostMapping("/updatemessage")
    public Result<?> updatemessage(@RequestParam("userId") String XX,
                                   @RequestParam("password") String password,
                                   @RequestParam("username") String username,
                                   @RequestParam("userTel") String userTel,
                                   @RequestParam("userMail") String userMail) throws IOException {
        System.out.println("updatemessage");
        user u= (user) request.getSession().getAttribute("user");
        String path="1";
        if(null==u){
            return Result.error("-1","Session过期");
        }
            user usertemp=usermapper.loginUser(u.getUserId());
//            String fileName = image.getOriginalFilename(); // 获取原始文件名
//            String suffix = fileName.substring(fileName.lastIndexOf(".")); // 获取文件后缀名
//            String newFileName = UUID.randomUUID().toString() + suffix; // 生成新的文件名
//            path="C:/Project/user/"+u.getUserId()+"/"+newFileName;
//            Files.copy(image.getInputStream(), Paths.get(path));
//            image.getInputStream().close();
            user userParam=new user(u.getUserId(),password,username,userTel,userMail,path);
            usermapper.UpdateUser(userParam);
            //还要在这里删除原图片
            System.out.println(usertemp.getUserAvatarUrl());
//            if(new File(usertemp.getUserAvatarUrl()).exists()){
//                File file=new File(usertemp.getUserAvatarUrl());
//                if (file.delete()) {
//                    user sessionmessage=usermapper.loginUser(u.getUserId());
//                    HttpSession session=request.getSession();
//                    session.setAttribute("user",sessionmessage);
//                    return Result.success();
//                } else {
//                    return Result.error("-1","文件删除失败");
//                }
//            }
        user sessionmessage=usermapper.loginUser(u.getUserId());
        HttpSession session=request.getSession();
        session.setAttribute("user",sessionmessage);
        return Result.success();
    }
//    @PostMapping("/updatemessage")
//    public Result<?> updatemessage(@RequestParam("userId") String XX,
//                                   @RequestParam("password") String password,
//                                   @RequestParam("username") String username,
//                                   @RequestParam("userTel") String userTel,
//                                   @RequestParam("userMail") String userMail,
//                                   @RequestParam(value = "image",required = false) MultipartFile image) throws IOException {
//        System.out.println("updatemessage");
//        user u= (user) request.getSession().getAttribute("user");
//        String path="";
//        if(null==u){
//            return Result.error("-1","Session过期");
//        }
//        if(image==null){
//            user userParam=new user(u.getUserId(),password,username,userTel,userMail,path);
//            usermapper.UpdateUser(userParam);
//        }else{
//            Resource resource = (Resource) resourceLoader.getResource("file:/path/to/save/" + image.getOriginalFilename());
//            image.transferTo(((org.springframework.core.io.Resource) resource).getFile());
//            user usertemp=usermapper.loginUser(u.getUserId());
//            String fileName = image.getOriginalFilename(); // 获取原始文件名
//            String suffix = fileName.substring(fileName.lastIndexOf(".")); // 获取文件后缀名
//            String newFileName = UUID.randomUUID().toString() + suffix; // 生成新的文件名
//            path="C:/Project/user/"+u.getUserId()+"/"+newFileName;
//            Files.copy(image.getInputStream(), Paths.get(path));
//            image.getInputStream().close();
//            user userParam=new user(u.getUserId(),password,username,userTel,userMail,path);
//            usermapper.UpdateUser(userParam);
//            //还要在这里删除原图片
//            System.out.println(usertemp.getUserAvatarUrl());
//            if(new File(usertemp.getUserAvatarUrl()).exists()){
//                File file=new File(usertemp.getUserAvatarUrl());
//                if (file.delete()) {
//                    user sessionmessage=usermapper.loginUser(u.getUserId());
//                    HttpSession session=request.getSession();
//                    session.setAttribute("user",sessionmessage);
//                    return Result.success();
//                } else {
//                    return Result.error("-1","文件删除失败");
//                }
//            }
//        }
//        user sessionmessage=usermapper.loginUser(u.getUserId());
//        HttpSession session=request.getSession();
//        session.setAttribute("user",sessionmessage);
//        return Result.success();
//    }
    @PostMapping("/test")
    public Result<?> SessionTest(){
        System.out.println("test");
        user u= (user) request.getSession().getAttribute("user");
        if(null==u){
            return Result.error("-1","Session过期");
        }else {
            return Result.success(u.getUserId());
        }
    }
}
