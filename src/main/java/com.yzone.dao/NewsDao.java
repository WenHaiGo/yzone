package com.yzone.dao;

import com.yzone.model.Comment;
import com.yzone.model.Language;
import com.yzone.model.News;
import com.yzone.model.NewsFlow;

import java.util.List;
import java.util.Map;

public interface NewsDao {
    List<Language> getAllLanguage();
    String getShortByComplete(String completeName);

    int add(News news);
/*这里的方法是不可以重载的*/
    //这个是用户首页展示的
    List<News> getAllNews(int uid);

    //这个是用户搜索的时候从数据库里直接得到所有的.
    List<News> getAllNews4S();

    int deleteById(int delteNewsId);

    int likeById(Map<String ,Integer> map);

    int isClickLike(Map<String, Integer> map);

    int changeLikeById(Map<String, Integer> map);

    Object getIsLike(Map<String, Integer> map);

    int commentNews(Comment comment);

    List<Comment> getCommentById(int newsId);

    List<News> getPageNews(Map<String,Integer> map);
}
