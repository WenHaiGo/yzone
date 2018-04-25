package com.yzone.controller;


import com.google.gson.Gson;
import com.yzone.model.User;
import com.yzone.service.UserService;
import com.yzone.utils.PersonPage;
import com.yzone.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 检查用户名是否和数据库匹配
     *
     * @param username
     * @return
     */
    @RequestMapping("/checkUserName")
    @ResponseBody
    public String isExist(String username) {
        System.out.println("dsdsd" + userService.isExist(username));
        return userService.isExist(username) == 1 ? "yes" : "no";
    }

    /**
     * 检查登陆是否成功
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/checkLogin")
    @ResponseBody
    public String checkLogin(HttpServletResponse response, String username, String password) {
        int isLogin = userService.checkLogin(username, password);
        if (isLogin == 1) {
            System.out.println("已经保存");
            //通过username获取user实体类
            User user = userService.getUserByUsername(username);
            String headPortait = user.getHeadPortrait();
            Cookie cookie = new Cookie("headPortait", user.getHeadPortrait());
            System.out.println("打印粗来用户头像" + user.getHeadPortrait());
            Cookie cookie1 = new Cookie("username", username);
            cookie.setPath("/");
            cookie1.setPath("/");
            cookie.setMaxAge(1000 * 60 * 60);
            cookie1.setMaxAge(1000 * 60 * 60);
            response.addCookie(cookie);
            response.addCookie(cookie1);
        }
        return isLogin == 1 ? "yes" : "no";
    }

    @RequestMapping("/register")
    @ResponseBody
    public String register(String username, String password) {

        return userService.register(username, password) == 1 ? "yes" : "no";
    }

    @RequestMapping("/quit")
    public String quit(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] allCookies = request.getCookies();
        if (allCookies != null) {
            //从中取出cookie
            for (int i = 0; i < allCookies.length; i++) {

                //依次取出
                Cookie temp = allCookies[i];

                if (temp.getName().equals("username")) {
                    //将该cookie删除
                    Cookie newCookie = new Cookie("username", null);      //假如要删除名称为username的Cookie
                    newCookie.setMaxAge(0);             //立即删除型
                    newCookie.setPath("/");               //项目所有目录均有效，这句很关键，否则不敢保证删除
                    response.addCookie(newCookie);     //重新写入，将覆盖之前的
                    continue;
                }

                if (temp.getName().equals("headPortait")) {
                    Cookie newCookie = new Cookie("headPortait", null);      //假如要删除名称为username的Cookie
                    newCookie.setMaxAge(0);             //立即删除型
                    newCookie.setPath("/");               //项目所有目录均有效，这句很关键，否则不敢保证删除
                    response.addCookie(newCookie);     //重新写入，将覆盖之前的
                    continue;
                }
            }

        }
        return "redirect:/";
    }
    @RequestMapping("/person")
    @ResponseBody
    public String getPerson(HttpServletRequest request, String userName) {
        //TODO抽象为一个工具类得到当前用户
        System.out.println("进来了" + userName.trim());
        String currentUserName = UserUtils.getCurrentUserName(request);
        System.out.println("当前用户是" + currentUserName);
        //如果是当前用户跳到用户自己的页面
        if (currentUserName.equals(userName)) {
            return "my";
        }
        //否则跳到另外一个页面
        else {
            return "other";
        }
    }
    @RequestMapping("/follow")
    @ResponseBody
    public String follow(String userName,HttpServletRequest request)
    {
        System.out.println("+++++++++++++++++++++++++"+"一寄给");
        User followUser = userService.getUserByUsername(userName);
        User user = userService.getUserByUsername(UserUtils.getCurrentUserName(request));
        int isFollow =  userService.followByUserName(user.getId(),followUser.getId());
        return isFollow==1?"yes":"no";
    }


/*这个好像没有用*/
    //TODO 这里的命名不规范  一会user  一会儿person
    @RequestMapping("/personpage")
    @ResponseBody
    public PersonPage getPersonInfo(String userName) {
        System.out.println(111111);
        PersonPage personPage =  userService.getPersonInfo(userName);
        return personPage;
    }



    @RequestMapping("/all")
    @ResponseBody
    public List<User> getAll(){
        System.out.println("sasas"+userService.getAll().get(0).getCreateTime());
       return userService.getAll();
    }


    @RequestMapping("/delete")
    @ResponseBody
    public String deleteByName(String userName){
        return userService.deleteByUid(userService.getUserByUsername(userName).getId())==1?"yes":"no";
    }
}
