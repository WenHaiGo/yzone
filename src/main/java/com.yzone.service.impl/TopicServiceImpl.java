package com.yzone.service.impl;

import com.yzone.dao.TopicDao;
import com.yzone.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    TopicDao topicDao;

    @Override
    public int add(int uid,String topicName,String description) {
         Map map = new HashMap<String,String>();
         map.put("topicName",topicName);
         map.put("description",description);
         map.put("uid",uid);
         return topicDao.add(map);
    }
}
