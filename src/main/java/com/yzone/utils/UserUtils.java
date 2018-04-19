package com.yzone.utils;

import com.yzone.model.User;
import com.yzone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


public class UserUtils {



    //得到当前用户
    public static String getCurrentUserName(HttpServletRequest request)
    {
        Cookie[] allCookies = request.getCookies();
        int uid = 0;
        //username在后面还是会用到
        String userName = null;
        if (allCookies != null) {
            //从中取出cookie
            for (int i = 0; i < allCookies.length; i++) {
                //依次取出
                Cookie temp = allCookies[i];
                if (temp.getName().equals("username")) {
                      return  temp.getValue();

                }
            }
        }
        return null;


    }
}
