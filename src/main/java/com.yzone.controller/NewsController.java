package com.yzone.controller;

import com.google.gson.Gson;
import com.yzone.model.*;
import com.yzone.service.NewsAndTopicService;
import com.yzone.service.NewsService;
import com.yzone.service.TopicService;
import com.yzone.service.UserService;
import com.yzone.translate.TransApi;
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
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

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
        //得到当前用户
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

        Gson gson = new Gson();

        System.out.println(gson.toJson(newsService.getAllNews(uid)));

        //不知道怎么把topic name 加到json里面, 只好新建一个类作为展示到信息流的实体
        //返回用户自己的消息和   TODO已经关注人以及比较热门的消息
        List<NewsFlow> list = newsService.getAllNews(uid);
        //遍历每一条消息,把是自己发的消息挑选出来,然后加上可以删除的属性.
        for (NewsFlow aNews : list
                ) {

            if (aNews.getUserName().equals(userName)) {
                aNews.setCanDelete(true);
                System.out.println("当前用户是" + userName);
            } else {
                aNews.setCanDelete(false);
            }
        }
        return list;
    }

    @RequestMapping("/delete")
    public String delete(String newsId) {
           return newsService.deleteById(newsId)==1?"yes":"no";

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
