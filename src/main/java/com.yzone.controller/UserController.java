package com.yzone.controller;


import com.google.gson.Gson;
import com.yzone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 检查用户名是否和数据库匹配
     * @param username
     * @return
     */
    @RequestMapping("/checkUserName")
    @ResponseBody
    public String isExist(String username) {
        System.out.println("nsiaouhuidfshoids");
        System.out.println(username+"============");
        System.out.println("dsdsd"+userService.isExist(username));
        return userService.isExist(username)==1?"yes":"no";
    }

    /**
     * 检查登陆是否成功
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/checkLogin")
    @ResponseBody
    public  String checkLogin(String username,String password)
    {
        System.out.println(username+"==============");
        System.out.println(password);
        return userService.checkLogin(username,password)==1?"yes":"no";
    }

}
