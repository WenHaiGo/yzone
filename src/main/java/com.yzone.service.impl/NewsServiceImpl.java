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

    @Autowired
    TopicService topicService;
    @Override
    public int save(News news) {
        JedisClientPool jedisClient= new JedisClientPool();

        int isSave =  newsDao.add(news);

        //是否在数据库保存成功
        if(isSave==1){
            //保存的时候就是直接按照这个来的,每次默认是1 ,
            jedisClient.zincrby("hotTopic",-1,topicService.getNameById(news.getTopicId()));
        }
        //这里不需要考虑redis是否保存成功了吗?
        return isSave;
    }


    @Autowired
    UserService userService;

    @Override
    public List<NewsFlow> getAllNews(int uid) {

        List<News> list = newsDao.getAllNews(uid);
        //将news转换为NewsFlow,直接传递给前面
        List<NewsFlow> newsFlows = new ArrayList<NewsFlow>();
        assignNewsFlow(list, newsFlows);
        return newsFlows;
    }
    @Override
    public List<NewsFlow> getAllNews4S() {

        List<News> list = newsDao.getAllNews4S();
        //将news转换为NewsFlow,直接传递给前面
        List<NewsFlow> newsFlows = new ArrayList<NewsFlow>();
        assignNewsFlow(list, newsFlows);
        return newsFlows;
    }

    @Override
    public List<NewsFlow> getPageNews(int pageNo, int pageSize,int uid) {
        int startNo = (pageNo -1)*pageSize;
        Map<String,Integer> map = new HashMap<>();
        map.put("pageNo",startNo);
        map.put("pageSize",pageSize);
        map.put("uid",uid);
        List<News> list =  newsDao.getPageNews(map);
        List<NewsFlow> newsFlow = new ArrayList<>();
        assignNewsFlow(list,newsFlow);
        return newsFlow;

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


    private void assignNewsFlow(List<News> list, List<NewsFlow> newsFlows) {
        for (News temp : list) {
            NewsFlow nf = new NewsFlow();
            nf.setNewsId(temp.getId());
            if(temp.getAddition()!=null){
                nf.setAddition(temp.getAddition());

            }

              nf.setCreateTime(temp.getCreateTime());
            if(temp.getMediaUrl()!=null){
                nf.setMediaUrl(temp.getMediaUrl());

            }

            nf.setOriginContent(temp.getOriginContent());
            nf.setTransContent(temp.getTransContent());
            if(temp.getTitle()!=null){
                nf.setTopicName(temp.getTitle());
            }
            nf.setTopicName(topicService.getNameById(temp.getTopicId()));
            //通过用户ID得到用户名和用户的个性签名  还有用户的头像
            int uid = temp.getUid();
            User user = userService.getUserById(uid);
            if(user.getPersonSignature()!=null){
                nf.setPersonSignature(user.getPersonSignature());

            }
            if(user.getHeadPortrait()!=null){
                nf.setHeadPortrait(user.getHeadPortrait());

            }
            nf.setUserName(user.getUsername());
            newsFlows.add(nf);
        }
    }

    @Override
    public List<News> getAllNews() {
        return newsDao.getAllNewsManage();
    }
}
