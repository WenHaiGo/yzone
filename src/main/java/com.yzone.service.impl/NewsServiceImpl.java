package com.yzone.service.impl;

import com.yzone.dao.NewsDao;
import com.yzone.model.Language;
import com.yzone.model.News;
import com.yzone.model.NewsFlow;
import com.yzone.model.User;
import com.yzone.service.NewsService;
import com.yzone.service.TopicService;
import com.yzone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class NewsServiceImpl implements NewsService {
   @Autowired
    NewsDao newsDao;
    @Override
    public List<Language> getAllLanguage() {
        return newsDao.getAllLanguage();
    }

    @Override
    public String getShortByComplete(String completeName) {
        return newsDao.getShortByComplete(completeName);
    }

    @Override
    public int save(News news) {
        return  newsDao.add(news);

    }


    @Autowired
    TopicService topicService;
    @Autowired
    UserService userService;
    @Override
    public List<NewsFlow> getAllNews(int uid) {

         List<News> list= newsDao.getAllNews(uid);
         //将news转换为NewsFlow,直接传递给前面
        List<NewsFlow> newsFlows =  new ArrayList<NewsFlow>();
        for (News temp:list) {
            NewsFlow nf = new NewsFlow();
            nf.setNewsId(temp.getId());
            nf.setAddition(temp.getAddition());
            nf.setCreateTime(temp.getCreateTime());
            nf.setMediaUrl(temp.getMediaUrl());
            nf.setOriginContent(temp.getOriginContent());
            nf.setTransContent(temp.getTransContent());
            nf.setTopicName(temp.getTitle());
            nf.setTopicName(topicService.getNameById(temp.getTopicId()));
            //通过用户ID得到用户名和用户的个性签名  还有用户的头像
            User user = userService.getUserById(temp.getUid());
            nf.setPersonSignature(user.getPersonSignature());
            nf.setHeadPortrait(user.getHeadPortrait());
            nf.setUserName(user.getUsername());
            newsFlows.add(nf);
        }
        return newsFlows;
    }

    @Override
    public int deleteById(String newsId) {
        int delteNewsId = Integer.parseInt(newsId);
        return newsDao.deleteById(delteNewsId);
    }
}
