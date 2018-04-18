package com.yzone.service.impl;

import com.yzone.dao.NewsAndTopicDao;
import com.yzone.model.NewsAndTopic;
import com.yzone.service.NewsAndTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class NewsAndTopicServiceImpl implements NewsAndTopicService {

    @Autowired
    NewsAndTopicDao newsAndTopicDao;
    @Override
    public int add(int newsId, int topicId) {
        HashMap<String,Integer> map = new HashMap<String, Integer>();
        map.put("newsId",newsId);
        map.put("topicId",topicId);
        return newsAndTopicDao.add(map);
    }
}
