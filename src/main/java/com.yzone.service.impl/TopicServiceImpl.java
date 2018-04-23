package com.yzone.service.impl;

import com.yzone.dao.TopicDao;
import com.yzone.model.FollowTopic;
import com.yzone.model.Topic;
import com.yzone.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    TopicDao topicDao;


    //TODO 这里是个事务操作
    @Override
    public int add(int uid,String topicName,String description) {
         Map map = new HashMap<String,String>();
         map.put("topicName",topicName);
         map.put("description",description);
         map.put("uid",uid);
         //添加的同时会关注
        int isAdd =  topicDao.add(map);
        if(isAdd>0)
        {
            System.out.println(topicName);
            int a = getTopicByName(topicName).getId();
            addFollower(uid,a);
        }
         return isAdd;
    }

    private int addFollower(int uid, int topicId) {
        FollowTopic followTopic = new FollowTopic();
        followTopic.setTopicId(topicId);
        followTopic.setUid(uid);
        return  topicDao.addFollower(followTopic);
    }

    @Override
    public List<Topic> fuzzyQuery(String key) {
        return topicDao.fuzzyQuery(key);
    }

    @Override
    public Topic getTopicByName(String topicName) {
        return  topicDao.getTopicByName(topicName);

    }

    @Override
    public String getNameById(int topicId) {
        return topicDao. getNameById(topicId);
    }
}
