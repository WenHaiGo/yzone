package com.yzone.service.impl;

import com.yzone.dao.NewsDao;
import com.yzone.model.Language;
import com.yzone.model.News;
import com.yzone.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
