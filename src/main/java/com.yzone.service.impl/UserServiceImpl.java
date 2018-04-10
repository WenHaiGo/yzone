package com.yzone.service.impl;

import com.yzone.dao.UserDao;
import com.yzone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public int isExist(String username) {
        int isExist = userDao.isExist(username);
        return isExist;
    }

    @Override
    public int checkLogin(String username, String password) {
        Map<String,String> map = new HashMap<String, String>();
        map.put("username",username);
        map.put("password",password);
        int isExist = userDao.checkLogin(map);
        return isExist;
    }
}
