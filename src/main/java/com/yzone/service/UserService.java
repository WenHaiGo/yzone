package com.yzone.service;

import com.yzone.model.User;

public interface UserService {

    int isExist(String username);

    int checkLogin(String username,String password);

    int register(String username,String password);

    User getUserByUsername(String username);

    User getUserById(int uid);
}
