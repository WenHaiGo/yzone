package com.yzone.service;

import com.yzone.model.Language;
import com.yzone.model.News;

import java.util.List;

public interface NewsService {
    List<Language> getAllLanguage();
    String getShortByComplete(String completeName);

    int save(News news);
}
