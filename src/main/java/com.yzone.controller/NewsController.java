package com.yzone.controller;

import com.google.gson.Gson;
import com.yzone.model.*;
import com.yzone.service.NewsAndTopicService;
import com.yzone.service.NewsService;
import com.yzone.service.TopicService;
import com.yzone.service.UserService;
import com.yzone.translate.TransApi;
import com.yzone.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/news")
public class NewsController {
    @Autowired
    NewsService newsService;

    @RequestMapping("/language")
    @ResponseBody
    public List<Language> getAllLanguage() {
        return newsService.getAllLanguage();
    }

    @RequestMapping("/trans")
    @ResponseBody
    public String getTransResult(String transLanguage, String originLanguage, String originContent) {
        //使用百度翻译api进行翻译
        final String APP_ID = "20180119000117102";
        final String SECURITY_KEY = "PrUuzyAWuXxi8Cpm4ubM";
        //得到相应的翻译语言
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String shortOrigin = newsService.getShortByComplete(originLanguage);
        String shortTrans = newsService.getShortByComplete(transLanguage);
        String result = null;
        try {
            result = api.getTransResult(originContent, shortOrigin, shortTrans);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("翻译结构是" + result);
        return result;
    }


    @Autowired
    UserService userService;
    @Autowired
    TopicService topicService;
    @Autowired
    NewsAndTopicService newsAndTopicService;

    @RequestMapping("/send")
    @ResponseBody
    public String send(String theFilePath, News news, MultipartFile file, String allChosenTopics, HttpServletRequest request, String transContent, String originContent, String addition) throws IOException {
        System.out.println("文件上传路径是" + theFilePath);
        String path = request.getSession().getServletContext().getRealPath("upload");

        String fileName = file.getOriginalFilename();
        String filetype = fileName.substring(fileName.lastIndexOf("."));

        String media = UUID.randomUUID().toString() + filetype;
        String mediaUrl = "upload" + "/" + media;
        File dir = new File(path, media);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        news.setAddition(addition);
        news.setOriginContent(originContent);
        news.setTransContent(transContent);
        news.setMediaUrl(mediaUrl);
        //保存用户id
        Cookie[] allCookies = request.getCookies();
        int uid = 0;
        if (allCookies != null) {
            //从中取出cookie
            for (int i = 0; i < allCookies.length; i++) {
                //依次取出
                Cookie temp = allCookies[i];
                if (temp.getName().equals("username")) {
                    String userName = temp.getValue();
                    User user = userService.getUserByUsername(userName);
                    if (user != null) {
                        uid = user.getId();
                        news.setUid(uid);
                    }
                }
            }
        }
        String[] temptopics = allChosenTopics.split(",");
        //为了去重
        HashSet<String> topicsSet = new HashSet<String>();
        for (String temp : temptopics) {
            topicsSet.add(temp);
        }

        //TODO这里只是为了演示临时写的程序,没有做多都跳news和topic做关联,.只是取除了第一个话题记录到了new里面,其实news里面
        //不应该存放topic_id的,,而是应该个news_toipc关联查询
        Topic temptopic = topicService.getTopicByName(temptopics[0]);
        if (temptopic != null) {
            news.setTopicId(temptopic.getId());
        }

        System.out.println("目前的topic_id是" + news.getTopicId());
        newsService.save(news);
        //这里话题和news是多对多的.
        //这里还要根据topic的名字获取对应ID;插入到关联表中
        for (String topicName : topicsSet) {
            Topic topic = topicService.getTopicByName(topicName);
            if (topic != null) {
                newsAndTopicService.add(news.getId(), topic.getId());
            }
        }
        //MultipartFile自带的解析方法
        file.transferTo(dir);
        //不管多措直接跳转到首页
        return "yes";
    }

    //页面news流展示
    @RequestMapping("/all")
    @ResponseBody
    public List<NewsFlow> getFlow(HttpServletRequest request) {
        ////TODO抽象为工具得到当前用户
        Cookie[] allCookies = request.getCookies();
        int uid = 0;
        //username在后面还是会用到
        String userName = null;
        if (allCookies != null) {
            //从中取出cookie
            for (int i = 0; i < allCookies.length; i++) {
                //依次取出
                Cookie temp = allCookies[i];
                if (temp.getName().equals("username")) {
                    userName = temp.getValue();
                    User user = userService.getUserByUsername(userName);
                    //得到当前用户的ID,
                    uid = user.getId();
                }
            }
        }
        //不知道怎么把topic name 加到json里面, 只好新建一个类作为展示到信息流的实体
        //返回用户自己的消息和   TODO已经关注人以及比较热门的消息
        List<NewsFlow> list = newsService.getAllNews(uid);
        //遍历每一条消息,把是自己发的消息挑选出来,然后加上可以删除的属性.
        /* TODO 这里是否点赞或者点踩一定要在Controler里面完成,因为在全文检索里面会用到这个方法*/
        for (NewsFlow aNews : list
                ) {

            int isLike = newsService.getIsLike(aNews.getNewsId(), aNews.getUserName());
            if (isLike == 1) {
                aNews.setLike(true);
            } else {
                aNews.setLike(false);
            }

            if (aNews.getUserName().equals(userName)) {
                aNews.setCanDelete(true);
            } else {
                aNews.setCanDelete(false);
            }
        }
        return list;
    }


    @RequestMapping("/manageAll")
    @ResponseBody
    public List<News> getAllNews() {
        return newsService.getAllNews();
    }


    @RequestMapping("/pageNews")
    @ResponseBody
    //这里直接固定了每一页展示多少个数据,所以就不传参数了
    public List<NewsFlow> getPageNews(int pageNo, HttpServletRequest request) {
        int pageSize = 5;
        System.out.println("输出页数"+pageNo);
        String userName = UserUtils.getCurrentUserName(request);
        //不知道怎么把topic name 加到json里面, 只好新建一个类作为展示到信息流的实体
        //返回用户自己的消息和   TODO已经关注人以及比较热门的消息
        List<NewsFlow> list = newsService.getPageNews(pageNo, pageSize, userService.getUserByUsername(userName).getId());

        //遍历每一条消息,把是自己发的消息挑选出来,然后加上可以删除的属性.
        for (NewsFlow aNews : list
                ) {
            int isLike = newsService.getIsLike(aNews.getNewsId(), aNews.getUserName());
            if (isLike == 1) {
                aNews.setLike(true);
            } else {
                aNews.setLike(false);
            }
            if (aNews.getUserName().equals(userName)) {
                aNews.setCanDelete(true);
            } else {
                aNews.setCanDelete(false);
            }
        }
        return list;
    }


    //TODO 这里可以返回一个字符串吗,不会被视图解析器捕获吗
    @RequestMapping("/delete")
    public String delete(String newsId) {
        return newsService.deleteById(newsId) == 1 ? "yes" : "no";
    }


    //处理点赞
    @RequestMapping("/like")
    @ResponseBody
    public String like(String userName, int newsId) {
        return newsService.likeById(userName, newsId);
    }


    @RequestMapping("/comment")
    @ResponseBody
    public String comment(String userName, int newsId, String content) {
        System.out.println("进俩了");
        return newsService.commentNews(userName, newsId, content);
    }


    @RequestMapping("/getComment")
    @ResponseBody
    public List<Comment> getComment(int newsId) {
        System.out.println("sasasasasasas");
        List<Comment> list = newsService.getCommentById(newsId);
        return newsService.getCommentById(newsId);
    }

    /**
     * 文件下载功能
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/down")
    public void down(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //模拟文件，myfile.txt为需要下载的文件
        String fileName = request.getSession().getServletContext().getRealPath("upload") + "/myfile.txt";
        //获取输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(new File(fileName)));
        //假如以中文名下载的话
        String filename = "下载文件.txt";
        //转码，免得文件名中文乱码
        filename = URLEncoder.encode(filename, "UTF-8");
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while ((len = bis.read()) != -1) {
            out.write(len);
            out.flush();
        }
        out.close();
    }


}
