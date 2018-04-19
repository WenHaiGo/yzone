package com.yzone.dao;

import com.yzone.model.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    /**
    检测用户名是否存在
     */
    int isExist(String username);

    /**
     * 检测是否登录正确
     * @return
     */
    int checkLogin(Map<String,String> map);

    /**
     * 注册
     * @return
     */
    int register(Map<String,String> map);

    User getUserByUsername(String username);

    User getUserById(int uid);

    List<User> fuzzyQuery(String key);

    int followByUserName(Map<String,Integer> map);
}
