package com.yzone.controller;

import com.google.gson.Gson;
import com.yzone.model.Language;
import com.yzone.model.News;
import com.yzone.service.NewsService;
import com.yzone.service.UserService;
import com.yzone.translate.TransApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
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


    @RequestMapping("/send")
    public String send(News news, MultipartFile file, HttpServletRequest request, String transContent, String originContent, String addition) throws IOException {
        System.out.println("++++++++++++++++++++++" + transContent + originContent);
        String path = request.getSession().getServletContext().getRealPath("upload");
        String name = file.getOriginalFilename();
        String filetype = name.substring(name.lastIndexOf("."));
        String mediaUrl = UUID.randomUUID().toString() + filetype;
        File dir = new File(path, mediaUrl);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        news.setAddition(addition);
        news.setOriginContent(originContent);
        news.setTransContent(transContent);
        news.setMediaUrl(mediaUrl);
        newsService.save(news);
        //MultipartFile自带的解析方法
        file.transferTo(dir);
        //跳转到首页
        return "redirect:/";
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
