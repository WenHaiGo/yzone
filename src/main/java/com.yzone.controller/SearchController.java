package com.yzone.controller;


import com.yzone.model.NewsFlow;
import com.yzone.utils.AllSearchResult;
import com.yzone.model.News;
import com.yzone.service.SearchService;
import com.yzone.utils.SearchRecomModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/search")
public class SearchController {


    @Autowired
    SearchService searchService;

    @RequestMapping("/recom")
    @ResponseBody
    public SearchRecomModel getSearchRecom(String key) {
        return searchService.getSearchRecom(key);
    }


    //先把单个数据库的实现了,然后再考虑如何实现多个表
    @RequestMapping("/all")
    @ResponseBody
    public AllSearchResult<News> searchByKey(String key) {
        return null;
    }

//建立索引

    @RequestMapping("/buildIndex.action")
    public void buildIndex() throws Exception{
        System.out.println("99999999999999999999");
        searchService.rebuildAllIndex();
    }

    //ajax调用这个方法
    @RequestMapping("/search.action")
    public @ResponseBody AllSearchResult<NewsFlow> search(String keyWord,int pageNo) throws UnsupportedEncodingException {
//        keyword = new String(keyword.getBytes("ISO-8859-1"),"UTF-8");
//        System.out.println(" =====  " + keyword);
        //ModelAndView mv = new ModelAndView() ;
        //对结果进行了分页处理,但是如何来调用这是个问题
        System.out.println(keyWord);
        System.err.println("哈哈"+pageNo);
        int pageSize =3;
        AllSearchResult<NewsFlow> lr = searchService.doSeacher(keyWord, pageNo,pageSize) ;
        return lr ;
    }

}
