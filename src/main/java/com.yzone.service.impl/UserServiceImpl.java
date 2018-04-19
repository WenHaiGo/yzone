package com.yzone.service.impl;

import com.yzone.dao.UserDao;
import com.yzone.model.User;
import com.yzone.service.UserService;
import com.yzone.utils.PersonPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);
        int isExist = userDao.checkLogin(map);
        return isExist;
    }

    @Override
    public int register(String username, String password) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);
        return userDao.register(map);

    }

    @Override
    public User getUserByUsername(String username) {

        return userDao.getUserByUsername(username);
    }

    @Override
    public User getUserById(int uid) {
        return userDao.getUserById(uid);
    }



    @Override
    public PersonPage getPersonInfo(String userName) {

        User user = getUserByUsername(userName);
        PersonPage p = new PersonPage();
        p.setUsername(userName);
        p.setHeadPortrait(user.getHeadPortrait());
        p.setPersonSignature(user.getPersonSignature());
        p.setProfession(user.getProfession());
        p.setAddress(user.getAddress());
        p.setSex(user.getSex());
        p.setuId(user.getId());
        return p;
    }

    @Override
    public List<User> fuzzyTuery(String key) {

        return userDao.fuzzyQuery(key);
    }

    @Override
    public int followByUserName(int uid,int followUid) {
        HashMap<String,Integer> map = new HashMap<>();
        map.put("uid",uid);
        map.put("followUid",followUid);
        System.out.println("wode id"+uid+"别人的Id"+followUid);
        return userDao.followByUserName(map);
    }
}
