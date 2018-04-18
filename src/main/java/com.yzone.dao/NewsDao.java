package com.yzone.dao;

import com.yzone.model.Language;
import com.yzone.model.News;

import java.util.List;

public interface NewsDao {
    List<Language> getAllLanguage();
    String getShortByComplete(String completeName);

    int add(News news);

    List<News> getAllNews(int uid);

    int deleteById(int delteNewsId);
}
