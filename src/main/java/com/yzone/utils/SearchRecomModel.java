package com.yzone.utils;

import com.yzone.model.Topic;
import com.yzone.model.User;

import java.util.List;

public class SearchRecomModel {

    //用户相关
    List<User> user;

    //话题相关
    List<Topic> topic;
    //内容相关
    List<String> content;

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public List<Topic> getTopic() {
        return topic;
    }

    public void setTopic(List<Topic> topic) {
        this.topic = topic;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }


}
