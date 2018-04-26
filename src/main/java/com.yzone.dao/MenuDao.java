package com.yzone.dao;

import com.yzone.model.Menu;

import java.util.List;

public interface MenuDao {
    //通过pid来获取所有的一级菜单
    List<Menu> getByPid(int pid);

    List<Menu> getByRoleId(int roleId);
}
