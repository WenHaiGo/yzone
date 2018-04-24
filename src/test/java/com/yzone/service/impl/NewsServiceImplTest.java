package com.yzone.service.impl;

import com.yzone.model.NewsFlow;
import com.yzone.service.NewsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class NewsServiceImplTest {


    @Autowired
    NewsService newsService;
    @Test
    public void getAllLanguage() {
        System.out.println(newsService.getAllLanguage().size());
    }

    @Test
    public void getPageNews() {
        List<NewsFlow> list =  newsService.getPageNews(1,3,1);
        System.out.println("========"+list.get(1).getTransContent());
    }


    @Test
    public String ttetete(){
        return "redirect:/yzone/";
    }
}