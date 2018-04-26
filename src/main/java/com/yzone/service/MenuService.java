package com.yzone.service;

import com.yzone.utils.MenuUtil;

import java.util.List;

public interface MenuService {


    //读出父菜单
    List<MenuUtil>  getMenu();

    List<MenuUtil> getMenuByRoleId(int roleId);
}
