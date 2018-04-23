package com.yzone.service;

import com.yzone.model.News;
import com.yzone.utils.AllSearchResult;
import com.yzone.utils.SearchRecomModel;

public interface SearchService {


    public static final String FIELD_originContent = "originContent" ;
    public static final String FIELD_transContent= "transContent" ;
    public static final String FIELD_addition = "addition";



    // 索引存放目录
    public static final String INDEX_DIR = "D:\\index_dir";
    SearchRecomModel getSearchRecom(String key);
    void rebuildAllIndex() throws Exception;

    AllSearchResult<News> doSeacher(String keyword, int pageNo, int pageSize);
}
