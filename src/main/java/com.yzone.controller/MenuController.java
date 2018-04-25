package com.yzone.controller;


import com.yzone.service.MenuService;
import com.yzone.service.UserService;
import com.yzone.utils.MenuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;


    @RequestMapping("/all")
    @ResponseBody
    public List<MenuUtil> getMenu(){
        List<MenuUtil> list =  menuService.getMenu();
        return menuService.getMenu();
    }
}
