package com.yzone.controller;


import com.yzone.service.NewsService;
import com.yzone.service.SearchService;
import com.yzone.service.TopicService;
import com.yzone.service.UserService;
import com.yzone.utils.SearchRecomModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/search")
public class SearchController {


    @Autowired
    SearchService searchService;
    @RequestMapping("/recom")
    @ResponseBody
    public SearchRecomModel getSearchRecom(String key){
        return  searchService.getSearchRecom(key);
    }
}
