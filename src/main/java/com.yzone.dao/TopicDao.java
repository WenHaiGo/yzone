package com.yzone.dao;


import com.yzone.model.FollowTopic;
import com.yzone.model.Topic;

import java.util.List;
import java.util.Map;

public interface TopicDao {

    int add(Map<String,String> map);

    List<Topic> fuzzyQuery(String key);

    Topic getTopicByName(String topicName);

    String getNameById(int topicId);

    int addFollower(FollowTopic followTopic);
}
