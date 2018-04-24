package com.yzone.service;

import com.yzone.model.News;
import com.yzone.model.NewsFlow;
import com.yzone.utils.AllSearchResult;
import com.yzone.utils.SearchRecomModel;

public interface SearchService {


    public static final String FIELD_originContent = "originContent" ;
    public static final String FIELD_transContent= "transContent" ;
    public static final String FIELD_addition="addition";
    public static final String FIELD_ID = "id";
    public static final String FIELD_UID = "uid";
    public static final String FIELD_topicName = "topicName";
    public static final String FIELD_mediaUrl = "mediaUrl";
    public static final String FIELD_userName = "userName";
    public static final String FIELD_personSignature = "personSignature";
    public static final String FIELD_headPortrait = "headPortrait";
    public static final String FIELD_canDelete = "canDelete";
    public static final String FIELD_hate = "hate";
    public static final String FIELD_like = "like";
    public static final String FIELD_createTime = "createTime";



    // 索引存放目录
    public static final String INDEX_DIR = "D:\\index_dir";
    SearchRecomModel getSearchRecom(String key);
    void rebuildAllIndex() throws Exception;

    AllSearchResult<NewsFlow> doSeacher(String keyword, int pageNo, int pageSize);
}
