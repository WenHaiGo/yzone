package com.yzone.dao;

import com.yzone.model.Comment;
import com.yzone.model.Language;
import com.yzone.model.News;

import java.util.List;
import java.util.Map;

public interface NewsDao {
    List<Language> getAllLanguage();
    String getShortByComplete(String completeName);

    int add(News news);

    List<News> getAllNews(int uid);

    int deleteById(int delteNewsId);

    int likeById(Map<String ,Integer> map);

    int isClickLike(Map<String, Integer> map);

    int changeLikeById(Map<String, Integer> map);

    Object getIsLike(Map<String, Integer> map);

    int commentNews(Comment comment);

    List<Comment> getCommentById(int newsId);
}
