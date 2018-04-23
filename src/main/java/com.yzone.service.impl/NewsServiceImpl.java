package com.yzone.service.impl;

import com.yzone.dao.NewsDao;
import com.yzone.model.*;
import com.yzone.service.NewsService;
import com.yzone.service.TopicService;
import com.yzone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return newsDao.add(news);

    }


    @Autowired
    TopicService topicService;
    @Autowired
    UserService userService;

    @Override
    public List<NewsFlow> getAllNews(int uid) {

        List<News> list = newsDao.getAllNews(uid);
        //将news转换为NewsFlow,直接传递给前面
        List<NewsFlow> newsFlows = new ArrayList<NewsFlow>();
        for (News temp : list) {
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

    @Override
    public String likeById(String userName, int newsId) {
        int uid = 0;
        if (userName != null) {
            uid = userService.getUserByUsername(userName).getId();
        }
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("uid", uid);
        map.put("newsId", newsId);
        //先去检查是否赞过的状态
        //如果已经赞过那么修改为未被赞过 否则就添加  就是通过newsId和uid来查询
        int isDo = 0;
        //先判断用户是否操作过,
        if (newsDao.isClickLike(map) > 0) {
            isDo = newsDao.changeLikeById(map);
        } else {
            isDo = newsDao.likeById(map);
        }
        return isDo == 1 ? "yes" : "no";
    }

    @Override
    public int getIsLike(int newsId, String userName) {

        Map<String, Integer> map = new HashMap();
        map.put("newsId", newsId);
        map.put("uid", userService.getUserByUsername(userName).getId());
        Object temp = newsDao.getIsLike(map);

        return temp == null ? 0 : Integer.parseInt(temp.toString());
    }

    @Override
    public String commentNews(String userName, int newsId, String content) {
        //导致保存不进去
        /*Map<String, Object> map = new HashMap<>();
        map.put("uid", userService.getUserByUsername(userName).getId());
        map.put("newsId", newsId);
        map.put("content", content);*/
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setNewsId(newsId);
        comment.setUid(userService.getUserByUsername(userName).getId());
        return newsDao.commentNews(comment)==1?"yes":"no";
    }

    @Override
    public List<Comment> getCommentById(int newsId) {
         List<Comment> list=newsDao.getCommentById(newsId);

        for (Comment c:list
             ) {
            c.setUserName(userService.getUserById(c.getUid()).getUsername());
        }
         return list;
    }


}
