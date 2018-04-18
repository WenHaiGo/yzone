package com.yzone.service;

import com.yzone.model.Topic;

import java.util.List;

public interface TopicService {
    int add(int uid,String topicName,String description);

    List<Topic> fuzzyQuery(String key);

    //这里直接获取到了Topic,害怕之后会用到其他没有想到的字段
    Topic getTopicByName(String topicName);

    String getNameById(int topicId);
}
