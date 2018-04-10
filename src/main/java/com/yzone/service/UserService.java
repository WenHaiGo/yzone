package com.yzone.service;

public interface UserService {

    int isExist(String username);

    int checkLogin(String username,String password);
}
