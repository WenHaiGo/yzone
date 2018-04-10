package com.yzone.dao;

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
}
