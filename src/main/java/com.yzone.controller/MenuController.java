package com.yzone.controller;


import com.yzone.model.ManageUser;
import com.yzone.service.MenuService;
import com.yzone.service.UserService;
import com.yzone.utils.MenuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;


    @RequestMapping("/all")
    @ResponseBody
    public List<MenuUtil> getMenu(HttpSession session){
        List<MenuUtil> list =  menuService.getMenu();
        ManageUser user = (ManageUser)session.getAttribute("manageUser");

        return menuService.getMenuByRoleId(user.getRoleId());
    }
}
