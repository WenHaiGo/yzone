package com.yzone.service.impl;

import com.yzone.service.TopicService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class TopicServiceImplTest {

    @Autowired
    TopicService topicService;
    @Test
    public void fuzzyQuery() {
        System.out.println(topicService.fuzzyQuery("英").get(0).getTopicName());
    }
}