package com.yzone.service.impl;

import com.yzone.service.NewsService;
import com.yzone.service.SearchService;
import com.yzone.service.TopicService;
import com.yzone.service.UserService;
import com.yzone.utils.SearchRecomModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;
    @Autowired
    TopicService topicService;

    @Override
    public SearchRecomModel getSearchRecom(String key) {
        SearchRecomModel searchRecomModel = new SearchRecomModel();
        searchRecomModel.setUser(userService.fuzzyTuery(key));
        searchRecomModel.setTopic(topicService.fuzzyQuery(key));

        return searchRecomModel;
    }
}
