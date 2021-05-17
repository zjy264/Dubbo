package com.xxxx.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.xxxx.api.entity.Bookmain;
import com.xxxx.api.mapper.BookmainMapper;
import com.xxxx.api.service.BookmainService;
import com.xxxx.api.uilt.QiniuCloudUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Reference(interfaceClass = BookmainService.class)
    private BookmainService bookmainService;
    @Autowired(required = false)
    private BookmainMapper bookmainMapper;

    @PostMapping("/changepic")
    public void changePic(){
        List<Bookmain> bookmains=bookmainMapper.selectList(null);
        int size=bookmains.size()-1;
        for (int i=0;i<size;i++){
            String pic=bookmains.get(i).getPic();
            bookmains.get(i).setPic("https"+pic.substring(4));
        };
        System.out.println("完成1");
        for (int i=0;i<size;i++){
            System.out.println(bookmains.get(i));
        };
        bookmainService.updateBatchById(bookmains);
        System.out.println("完成2");
    }
    @PostMapping("/upload_image")
    public String uplload_image(@RequestBody Map<String,String> img){
        QiniuCloudUtil qiniuCloudUtil=new QiniuCloudUtil();
        return qiniuCloudUtil.uploadimage(img.get("image"),img.get("name"));
    }
//    @PostMapping("/getV")
//    public void getV(){
//        Spider.create(new GetVidio())
//                .setDownloader(new HttpClientDownloader())
//                .addUrl("https://www.bilibili.com/1c1a6d1e-7475-4d4a-ba6b-a0b26aa392c8")
//                //开启5个线程抓取
//                //启动爬虫
//                .start();
//    }
//    @PostMapping("/reqVidio")
//    public String Reqvidio(@RequestBody Map<String,String> url){
//        int size1=url.get("url").indexOf("BV");
//        int size2=url.get("url").indexOf("?");
//        String bv="";
//        BilibiliUtil bilibiliUtil=new BilibiliUtil();
//        if (size2==-1){
//            bv=url.get("url").substring(size1);
//        }else {
//            bv=url.get("url").substring(size1,size2);
//        }
//        String cid= JSONArray.parseObject(bilibiliUtil.getCid(bv)).getJSONArray("data").getJSONObject(0).get("cid").toString();
//        String aid= JSONArray.parseObject(bilibiliUtil.getAid(bv,cid)).getJSONObject("data").get("aid").toString();
//        String vidio= JSONArray.parseObject(bilibiliUtil.getVidio(cid,aid)).getJSONObject("data").getJSONArray("durl").getJSONObject(0).get("url").toString();
//        return vidio;
//    }
}
