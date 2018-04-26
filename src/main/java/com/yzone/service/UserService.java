package com.yzone.service;

import com.yzone.model.ManageUser;
import com.yzone.model.User;
import com.yzone.utils.PersonPage;

import java.util.List;

public interface UserService {

    int isExist(String username);

    int checkLogin(String username,String password);

    int register(String username,String password);

    User getUserByUsername(String username);

    User getUserById(int uid);

    PersonPage getPersonInfo(String userName);

    List<User> fuzzyTuery(String key);

    int followByUserName(int uid,int followUid);

    List<User> getAll();

    int deleteByUid(int uid);

    ManageUser manageLogin(String username, String password);
}
