package com.yzone.service;

import com.yzone.model.Comment;
import com.yzone.model.Language;
import com.yzone.model.News;
import com.yzone.model.NewsFlow;

import java.util.List;

public interface NewsService {
    List<Language> getAllLanguage();
    String getShortByComplete(String completeName);

    int save(News news);

    List<NewsFlow> getAllNews(int uid);

    int deleteById(String newsId);


    String likeById(String userName,int newsId);



    int getIsLike(int newsId, String userName);

    String commentNews(String userName, int newsId, String content);

    List<Comment> getCommentById(int newsId);
}
