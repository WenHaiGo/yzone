package com.yzone.controller;

import com.yzone.model.User;
import com.yzone.service.TopicService;
import com.yzone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    @ResponseBody
    public String addTopic(HttpServletRequest request, String topicName, String description) {
        int isAdd = 0;
        Cookie[] allCookies = request.getCookies();
        if (allCookies != null) {
            //从中取出cookie
            for (int i = 0; i < allCookies.length; i++) {

                //依次取出
                Cookie temp = allCookies[i];
                if (temp.getName().equals("username")) {
                    String userName =  temp.getValue();
                    System.out.println("大萨达所多所多所多所多所多所多所多所多所多所多所多所多所多所多所"+userName);
                    User user = userService.getUserByUsername(userName);
                    System.out.println(user.getId());
                    if(user!=null){
                        isAdd = topicService.add(user.getId(), topicName, description);
                    }

                }
            }

        }


        return isAdd == 1 ? "yes" : "no";
    }
}