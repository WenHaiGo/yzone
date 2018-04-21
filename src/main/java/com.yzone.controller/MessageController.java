package com.yzone.controller;

import com.google.gson.Gson;
import com.yzone.model.Message;
import com.yzone.model.User;
import com.yzone.service.MessageService;
import com.yzone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/message")
public class MessageController {


    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @RequestMapping("/unread")
    @ResponseBody
    public List<Message> getUnreadByUserame(String userName)
    {
        System.out.println("jianrule 未读消息");
        //TODO这里最好直接是byname  不要byid,不要在控制层处理这些
        User user = userService.getUserByUsername(userName);
        List<Message> list =   messageService.checkUnreadByUid(user.getId());//转换为字符串,不知道直接返回int注解会不会解析

        //给每一个message将uid转换为username
        for (Message m :list
             ) {

         m.setUserName((userService.getUserById(m.getUid())).getUsername());
        }


        return list;
    }
}
