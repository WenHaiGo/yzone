package com.yzone.service.impl;

import com.yzone.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Test
    public void isExist() {
        System.out.println(userService.isExist("john"));
    }

    @Test
    public void checkLogin() {
        System.out.println(userService.checkLogin("john","123"));
    }
}
