package com.yzone.service.impl;

import com.yzone.dao.MenuDao;
import com.yzone.model.Menu;
import com.yzone.service.MenuService;
import com.yzone.utils.MenuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {


    @Autowired
    MenuDao menuDao;


    @Override
    public List<MenuUtil> getMenu() {
//获取一级菜单
        List<Menu> fatherlist = menuDao.getByPid(0);

        List<MenuUtil> utilList = new ArrayList<>();
        for (int i = 0; i < fatherlist.size(); i++) {
            MenuUtil menuUtil = new MenuUtil();
            menuUtil.setList(menuDao.getByPid(fatherlist.get(i).getId()));
            menuUtil.setName(fatherlist.get(i).getName());
            //工具类还有得到id的必要吗
            menuUtil.setId(fatherlist.get(i).getId());
            menuUtil.setUrl(fatherlist.get(i).getUrl());
            utilList.add(menuUtil);
        }
        return utilList;
    }
}
