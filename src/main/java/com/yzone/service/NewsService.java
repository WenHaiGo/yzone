package com.yzone.service;

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
}
