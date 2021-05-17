package com.xxxx.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxxx.api.entity.Book;
import com.xxxx.api.entity.Bookmain;
import com.xxxx.api.entity.dto.GetNew;
import com.xxxx.api.mapper.BookMapper;
import com.xxxx.api.mapper.BookmainMapper;
import com.xxxx.api.service.BookmainService;
import com.xxxx.api.service.DataService;
import com.xxxx.api.uilt.Base64Util;
import com.xxxx.api.uilt.QiniuCloudUtil;
import com.xxxx.api.uilt.Result;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
@RestController
@RequestMapping("/bookmain")
public class BookmainController {
    @Autowired(required = false)
    private BookmainMapper bookmainMapper;
    @Reference(interfaceClass = BookmainService.class)
    private BookmainService bookmainService;
    @Autowired(required = false)
    private BookMapper bookMapper;
    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;
    @Reference(interfaceClass = DataService.class)
    private DataService dataService;
    public String change_pic(String url) {
        String imgurl=url;
        imgurl="http:"+imgurl;
        imgurl=imgurl.substring(0,imgurl.indexOf("?"));
        String km= Base64Util.GetImageStrFromUrl(imgurl);
        return km;
    }
    //将数据库中的图片网址转换为自己七牛云的图链
    @RequestMapping(value = "/change_pic")
    public void changekm() throws Exception {
        List<Bookmain> bookmains=bookmainMapper.selectList(null);
        int size=bookmains.size()-1;
        QiniuCloudUtil qiniuCloudUtil=new QiniuCloudUtil();
        for (int i=0;i<size;i++){
            qiniuCloudUtil.uploadimage(bookmains.get(i));
        };
        System.out.println("完成1");
        for (int i=0;i<size;i++){
            bookmains.get(i).setPic("https://qinui.zjykt.top/"+bookmains.get(i).getName());
        };
        bookmainService.updateBatchById(bookmains);
        System.out.println("完成2");
    }
    @RequestMapping(value = "/show")
    public Result show_news(@RequestBody(required = false) GetNew getNew) {
        Result result = new Result<>();
        HashMap<String,String> columnMap = new HashMap<>();
        QueryWrapper<Bookmain> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("name", );
        queryWrapper.like("name",getNew.getBookname());
        List<Bookmain> book = bookmainMapper.selectList(queryWrapper);
        if (book.size() == 0) {
            result.setCode("400");
            result.setMsg("失败");
        } else {
            result.setCode("200");
            result.setMsg("成功s");
            result.setData(book);
        }
        return result;
    }
    @RequestMapping(value = "/addZan")
    public void add_Zan(@RequestBody(required = false)GetNew getNew) {
        QueryWrapper<Book> queryWrapper1=new QueryWrapper<>();
        queryWrapper1.eq("name",getNew.getBookname());
        int zan=bookMapper.selectList(queryWrapper1).get(0).getZan()+1;//点击量
        UpdateWrapper<Book> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("name",getNew.getBookname()).set("zan",zan);
        bookMapper.update(null,updateWrapper);
    }
    //显示所有作者
    @RequestMapping(value = "/showauthor")
    public Result show_author() {
        Result result = new Result<>();
        if (dataService.checkData("book_author")){
            List<Bookmain> book2 =dataService.getBookData_T("book_author");
            result.setData(book2);
            result.setCode("200");
            result.setMsg("ok");
        }else {
            QueryWrapper<Bookmain> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("DISTINCT author");
            List<Bookmain> book2 = bookmainMapper.selectList(queryWrapper);
            result.setData(book2);
            result.setCode("200");
            result.setMsg("ok");
            HashMap<String,List<Bookmain>> hashMap=new HashMap<>();
            hashMap.put("book_author",book2);
            rabbitTemplate.convertAndSend("topicExchange","book.msg",hashMap);
        }
        return result;
    }
    //显示所有出版社
    @RequestMapping(value = "/showpublish")
    public Result show_publish() {
        Result result = new Result<>();
        if (dataService.checkData("book_publish")){
            List<Bookmain> book3 =dataService.getBookData_T("book_publish");
            result.setData(book3);
            result.setCode("200");
            result.setMsg("ok");
        }else {
            QueryWrapper<Bookmain> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("DISTINCT publish");
            List<Bookmain> book3 = bookmainMapper.selectList(queryWrapper);
            result.setData(book3);
            result.setCode("200");
            result.setMsg("ok");
            HashMap<String,List<Bookmain>> hashMap=new HashMap<>();
            hashMap.put("book_publish",book3);
            rabbitTemplate.convertAndSend("topicExchange","book.msg",hashMap);
        }
        return result;
    }
}
