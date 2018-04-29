package com.yzone.service.impl;

import com.yzone.dao.NewsDao;
import com.yzone.model.News;
import com.yzone.model.NewsFlow;
import com.yzone.service.NewsService;
import com.yzone.service.SearchService;
import com.yzone.service.TopicService;
import com.yzone.service.UserService;
import com.yzone.utils.AllSearchResult;
import com.yzone.utils.SearchRecomModel;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

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

    public Analyzer getAnalyzer() {

        return new StandardAnalyzer();
    }
    /** 打开索引的存放目录 */
    public Directory openDirectory() {
        try {
            System.out.println(new File(INDEX_DIR)  + "-------打开索引--------------");
            return FSDirectory.open(FileSystems.getDefault().getPath(INDEX_DIR));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//得到IndexWriter
    private IndexWriter getIndexWriter(){
        IndexWriter indexWriter = null;
        // 2、创建IndexWriter
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
                getAnalyzer());

        try {
            indexWriter = new IndexWriter(this.openDirectory(), indexWriterConfig);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return indexWriter ;
    }

    /** 对文件的指定属性映射成域,返回文件文档对象 */
    public Document createForumuploadDocument(NewsFlow news) {
        Document doc = new Document(); // 创建一个文档对象
        Field field1 = new Field(FIELD_transContent, String.valueOf(news.getTransContent()),TextField.TYPE_STORED);
        // 原文域
        Field field2 = new Field(FIELD_originContent, String.valueOf(news.getOriginContent()),TextField.TYPE_STORED);
        // addition域
        Field field3 = new Field(FIELD_addition, String.valueOf(news.getAddition()),TextField.TYPE_STORED);
        Field field4 = new Field(FIELD_mediaUrl, String.valueOf(news.getMediaUrl()),TextField.TYPE_STORED);
        Field field5 = new Field(FIELD_topicName, String.valueOf(news.getTopicName()),TextField.TYPE_STORED);
        Field field6 = new Field(FIELD_personSignature, String.valueOf(news.getPersonSignature()),TextField.TYPE_STORED);
        Field field7 = new Field(FIELD_userName, String.valueOf(news.getUserName()),TextField.TYPE_STORED);
        Field field9 = new Field(FIELD_ID, String.valueOf(news.getNewsId()),TextField.TYPE_STORED);
        Field field10 = new Field(FIELD_headPortrait, String.valueOf(news.getHeadPortrait()),TextField.TYPE_STORED);
        Field field11= new Field(FIELD_hate, String.valueOf(news.isHate()),TextField.TYPE_STORED);
        Field field12= new Field(FIELD_like, String.valueOf(news.isLike()),TextField.TYPE_STORED);
        Field field13= new Field(FIELD_canDelete, String.valueOf(news.isCanDelete()),TextField.TYPE_STORED);

        /*不知道如何使用这个来实现
        for(int i = 1;i<14;i++){
            doc.add(field);
        }
        */
        /*注意没有8*/
        doc.add(field1);
        doc.add(field2);
        doc.add(field3);
        doc.add(field4);
        doc.add(field5);
        doc.add(field6);
        doc.add(field7);
        doc.add(field9);
        doc.add(field10);
        doc.add(field11);
        doc.add(field12);
        doc.add(field13);
        return doc;
    }

    @Autowired
    NewsDao newsDao;
    @Override
    public void rebuildAllIndex() throws Exception {
        File file = new File(INDEX_DIR);
        //若存在就删除所有
        if (file.exists()) {
            for (File subFile : file.listFiles()) {
                subFile.delete();
            }
        } else {
            file.mkdirs();
        }
        List<NewsFlow> newsList = newsService.getAllNews4S();
        System.out.println(newsList.size()+"sasasasasasa");
        IndexWriter indexWriter = null;
        try {
            indexWriter = getIndexWriter() ;
            // 设置打开使用复合文件
            // indexWriter.setUseCompoundFile(true);
            int size = newsList == null ? 0 : newsList.size();
            for (int i = 0; i < size; i++) {
                NewsFlow n = newsList.get(i);
                Document doc = createForumuploadDocument(n);
                indexWriter.addDocument(doc);
                // if (i % 20 == 0) {
                indexWriter.commit();
                //}
            }
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (LockObtainFailedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (indexWriter != null) {
                    indexWriter.close();// 关闭IndexWriter,把内存中的数据写到文件
                }
            } catch (CorruptIndexException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public AllSearchResult<NewsFlow> doSeacher(String keyword, int pageNo, int pageSize) {
        //新建一个搜索结果集
        AllSearchResult<NewsFlow> lsr = new AllSearchResult<>();
        /*给结果集添加页数标志*/
        lsr.setPageNo(pageNo);
        lsr.setPageSize(pageSize);
        lsr.setKeyword(keyword);

        //TODO 为什么要在外面先定义  难道是要关闭吗类似流
        //用于搜索索引
        IndexSearcher searcher = null;
        //用于读取索引
        DirectoryReader directoryReader = null;
        try {
            // 1、创建Directory 会读取本地已经创建好的索引
            Directory directory = openDirectory() ;
            // 2、创建  打开directory
            directoryReader = DirectoryReader.open(directory);
            // 3、根据directoryReader创建IndexSearch  就是先去读,然后去检索索引
            searcher = new IndexSearcher(directoryReader);

            // 用多域查询解析器来创建一个查询器,TODO 没有理解
            Query query = MultiFieldQueryParser.parse(keyword, new String[] { FIELD_originContent, FIELD_transContent },
                    new BooleanClause.Occur[] {BooleanClause.Occur.SHOULD,BooleanClause.Occur.SHOULD }, this.getAnalyzer());

            long begin = System.currentTimeMillis();

            // 查询前100000个结果  TODO如何直接查询所有
            TopDocs ts = searcher.search(query, 100000);

            // 获取命中的数量 TODO 命中的数量代表什么?? 代表结果集中一共有多少数据吗
            lsr.setRecordCount(ts.totalHits);


            // 获取匹配到的结果集
            ScoreDoc[] hits = ts.scoreDocs;
            List<NewsFlow> ais = new ArrayList<>();

            //表示总页数int totalPages = totalNumData % pageSize == 0 ? totalNumData / pageSize : totalNumData / pageSize + 1;

            //int  totalNumData = lsr.getRecordCount();
            //TODO 如何检验这样是可以的?
            int pageCount = (lsr.getRecordCount() + pageSize - 1) / pageSize; // 总页数
            //表示从第几条数据到第几条数据
            int start = 0; // 要开始返回的文档编号
            int end = 0; // 要结束返回的文档编号
            if (pageCount > 0) {
                start = (pageNo - 1) * pageSize;
                end = start + pageSize;
//                if(start>lsr.getRecordCount()||end>lsr.getRecordCount())
//                {
//                    start
//                }
                if (pageNo == pageCount) { // 处理最后一页的结束文档的编号
                    end = start + (lsr.getRecordCount() % pageSize);
                }
            }

            //这是什么?TODO
            if (start < end) {
                lsr.setStratNo(start + 1);
                lsr.setEndNo(end);
            }
            System.out.println(" ===  start : " + start  + " ------    end : " + end);
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));

            for (int i = start; i < end; i++) { // 循环获取分页数据
                System.out.println("start : " + start  + " ------    end : " + end);
                // 通过内部编号从已经命中得获取文档
                Document doc = searcher.doc(hits[i].doc);
                /*把文档的数据放在news里面,就是为了可以把搜索到的数据存储起来*/
                NewsFlow news = new NewsFlow();
                // 处理文件名称的高亮显示问题
               String originContent = highlighter.getBestFragment(getAnalyzer(), FIELD_originContent,doc.getField(FIELD_originContent).stringValue());
               String transContent =  highlighter.getBestFragment(getAnalyzer(),FIELD_transContent,doc.getField(FIELD_transContent).stringValue());
               String addtion = highlighter.getBestFragment(getAnalyzer(),FIELD_addition,doc.getField(FIELD_addition).stringValue());

               if(originContent!=null){
                   news.setOriginContent(originContent);
               }
               else {
                   news.setOriginContent(doc.getField(FIELD_originContent).stringValue());
               }
               if(transContent!=null){
                   news.setTransContent(transContent);

               }
               else {
                   news.setTransContent(doc.getField(FIELD_transContent).stringValue());
               }
               if(addtion!=null){
                   news.setAddition(addtion);
               }
               else {
                   news.setAddition(doc.getField(FIELD_addition).stringValue());
               }

               news.setLike(doc.getField(FIELD_like).stringValue().equals("true")?true:false);
               news.setNewsId(Integer.parseInt(doc.getField(FIELD_ID).stringValue()));
               news.setCanDelete(doc.getField(FIELD_canDelete).stringValue().equals("true")?true:false);
               news.setUserName(doc.getField(FIELD_userName).stringValue());
               news.setHeadPortrait(doc.getField(FIELD_headPortrait).stringValue());
               news.setPersonSignature(doc.getField(FIELD_personSignature).stringValue());
               news.setTopicName(doc.getField(FIELD_topicName).stringValue());
               news.setMediaUrl(doc.getField(FIELD_mediaUrl).stringValue());
               //时间这里不好转换  先不管了,
               /*news.setCreateTime(doc.getField(FIELD_createTime).stringValue().);*/
               ais.add(news); // 把符合条件的数据添加到List
            }
            lsr.setTime((System.currentTimeMillis() - begin) / 1000.0); // 计算搜索耗时秒数  
            lsr.setDatas(ais); // 把查询到的数据添加到LuceneSearchResult  
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        } finally {
            if (directoryReader != null) {
                try {
                    directoryReader.close();
                    // 关闭搜索器  
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return lsr;
    }
}
