package com.xxxx.consumer.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.api.entity.Book;
import com.xxxx.api.entity.Bookmain;
import com.xxxx.api.entity.dto.BookVO;
import com.xxxx.api.entity.dto.NewsDto;
import com.xxxx.api.entity.dto.UpdataDto;
import com.xxxx.api.mapper.BookMapper;
import com.xxxx.api.mapper.BookmainMapper;
import com.xxxx.api.service.BookService;
import com.xxxx.api.service.BookmainService;
import com.xxxx.api.service.DataService;
import com.xxxx.api.uilt.Result;
import com.xxxx.api.uilt.Selectnews;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
@RequestMapping("/book")
public class BookController {
    @Reference(interfaceClass = BookService.class)
    private BookService bookService;

    @Reference(interfaceClass = DataService.class)
    private DataService dataService;

    @Autowired(required = false)
    private BookmainMapper bookmainMapper;

    @Reference(interfaceClass = BookmainService.class)
    private BookmainService bookmainService;

    @Autowired(required = false)
    private BookMapper bookMapper;

    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;
    @RequestMapping("/getBookIntroduction")
    public Result getBookIntroduction(@RequestBody Map<String,String> bookname){
        Result result=new Result();
        try{
            JSONArray jsonArray=JSONArray.parseArray(bookname.get("bookname"));
            List<Book> list=new ArrayList<>();
            for (int i=0;i<jsonArray.size();i++){
                System.out.println(jsonArray.getJSONObject(i).getString("name"));
                QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("name", jsonArray.getJSONObject(i).getString("name"));
                list.add(bookMapper.selectList(queryWrapper).get(0));
            }
            result.setCode("200");
            result.setData(list);
            return result;
        }catch (Exception e){
            QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", bookname.get("bookname"));
            List<Book> list = bookMapper.selectList(queryWrapper);
            result.setCode("200");
            result.setData(list);
            return result;
        }

    }
    @RequestMapping(value = "/show")
    public Result shownews() {
        Result<Object> result = new Result<>();
        if (dataService.checkData("book")){
            result.setData(dataService.getBookData("book"));
            result.setCode("200");
            result.setMsg("成功s");
        }else {
            List<BookVO> book = bookService.getBook(new Page<>(1, 500));
            result.setData(book);
            result.setCode("200");
            result.setMsg("成功");
            HashMap<String,List<BookVO>> hashMap=new HashMap<>();
            hashMap.put("book",book);
            rabbitTemplate.convertAndSend("topicExchange","book.msg",hashMap);
        }
        return result;
    }
    @RequestMapping(value = "/show_Luilan")
    public Result show_Luilan() {
        Result<List<BookVO>> result = new Result<>();
        if (dataService.checkData("book_luilan")) {
            result.setData(dataService.getBookData("book_luilan"));
            result.setCode("200");
            result.setMsg("成功");
        } else {
            List<BookVO> book=bookService.getBook_Liulan(new Page<>(1, 6));
            result.setCode("200");
            result.setMsg("成功s");
            result.setData(book);
            HashMap<String,List<BookVO>> hashMap=new HashMap<>();
            hashMap.put("book_luilan",book);
            //加入消息队列
            rabbitTemplate.convertAndSend("topicExchange","book.msg",hashMap);
        }
        return result;
    }
    @RequestMapping(value = "/show_Collect")
    public Result show_Collect() {
        Result<List<BookVO>> result = new Result<>();
        if (dataService.checkData("book_collect")) {
            result.setData(dataService.getBookData("book_collect"));
            result.setCode("200");
            result.setMsg("成功");
        } else {
            List<BookVO> book=bookService.getBook_Collect(new Page<>(1, 6));
            result.setCode("200");
            result.setMsg("成功s");
            result.setData(book);
            HashMap<String,List<BookVO>> hashMap=new HashMap<>();
            hashMap.put("book_collect",book);
            //加入消息队列
            rabbitTemplate.convertAndSend("topicExchange","book.msg",hashMap);
        }
        return result;
    }
    ///显示搜索建议
    @RequestMapping(value = "/showto")
    public Result shownews2() {
        Result<List<HashMap<String, String>>> result = new Result<>();
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        List<Book> book = bookMapper.selectList(queryWrapper);
        List<HashMap<String, String>> hashMaps = new ArrayList<>();
        for (Book bt : book
        ) {
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("value",bt.getName());
            hashMap.put("author",bt.getAuthor());
            hashMaps.add(hashMap);
        }
        if (hashMaps.size() == 0) {
            result.setCode("400");
            result.setMsg("失败");
        } else {
            result.setCode("200");
            result.setMsg("成功s");
            result.setData(hashMaps);
        }
        return result;
    }
    @RequestMapping(value = "/showtype")
    public Result showbook_type() {
        Result result=new Result();
        if (dataService.checkData("book_type")){
            List<Book> book=dataService.getBookData_s("book_type");
            result.setData(book);
            result.setCode("200");
        }else {
            QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("DISTINCT type");
            List<Book> book = bookMapper.selectList(queryWrapper);
            result.setData(book);
            result.setCode("200");
            HashMap<String,List<Book>> hashMap=new HashMap<>();
            hashMap.put("book_type",book);
            rabbitTemplate.convertAndSend("topicExchange","book.msg",hashMap);
        }
        return result;
    }
    @RequestMapping(value = "/updata")
    public JSONObject updata(@RequestBody(required = false) UpdataDto updataDto) throws AWTException {
        UpdateWrapper<Bookmain> updateWrapper2=new UpdateWrapper<>();
        UpdateWrapper<Book> updateWrapper3=new UpdateWrapper<>();
        updateWrapper2.eq("name",updataDto.getOld_name());
        updateWrapper3.eq("name",updataDto.getOld_name());
        Bookmain bookmain=new Bookmain();
        bookmain.setName(updataDto.getName());
        bookmain.setAuthor(updataDto.getAuthor());
        bookmain.setData(LocalDateTime.parse(updataDto.getData()));
        bookmain.setPic(updataDto.getPic());
        bookmain.setPublish(updataDto.getPublish());
        bookmain.setTranslator(updataDto.getTranslator());
        bookmain.setPrice(new BigDecimal(updataDto.getPrice()));
        System.out.println(bookmain);
        Book books=new Book();
        books.setType(updataDto.getType_name());
        books.setName(updataDto.getName());
        books.setAuthor(updataDto.getAuthor());
        books.setPrice(new BigDecimal(updataDto.getPrice()));
        books.setIntroduction(updataDto.getIntroduction());
        System.out.println(books);
        int rows2 = bookmainMapper.update(bookmain, updateWrapper2);
        int rows3 = bookMapper.update(books,updateWrapper3);
        JSONObject result = new JSONObject();
        result.put("code", "200");
        result.put("msg", "成功");
        result.put("data", updataDto);
        result.put("data2", rows2);
        result.put("data3", rows3);
        //这里添加消息队列  更新book
        List<BookVO> book = bookService.getBook(new Page<>(1, 500));
        HashMap<String,List<BookVO>> hashMap=new HashMap<>();
        hashMap.put("book",book);
        rabbitTemplate.convertAndSend("topicExchange","book.msg",hashMap);
        return result;
    }

    @RequestMapping(value = "/test")
    public int test() throws AWTException {
        System.out.println("使用图片");
        return 200;
    }

//    @RequestMapping(value = "/requirednews")
//    public void getNews(@RequestBody(required = false) NewsDto newsDto) {
//        con = 1;
//        startKt(newsDto.getUrl());
//        Timer timer = new Timer();// 实例化Timer类
//        timer.schedule(new TimerTask() {
//            public void run() {
//                System.out.println("------------开始mysql存储1---------------------");
//                Timer timer2 = new Timer();// 实例化Timer类
//                timer2.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        con = 0;
//                        for (String url : Selectnews.detil) {
//                            startKt(url);
//                        }
//                        Timer timer3 = new Timer();// 实例化Timer类
//                        timer3.schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                getky2(newsDto.getType_name());
////                                getky();
//                            }
//                        }, 5000);
//                    }
//                }, 5000);
//            }
//        }, 5000);// 这里百毫秒
//    }
//
//    public void getky() {
//        System.out.println("输出");
//        for (int i = 0; i < Selectnews.list1.size(); i++) {
//            System.out.println(Selectnews.list1.get(i));//bookname
//            System.out.println(Selectnews.list2.get(i));//作者
//            System.out.println(Selectnews.list3.get(i));//price
//            BigDecimal bd = new BigDecimal(Selectnews.list3.get(i).substring(1));
//            System.out.println(bd);
//            System.out.println(Selectnews.list4.get(i));//出版日期
//            System.out.println("http:" + Selectnews.list5.get(i));
//            if (Selectnews.list6.get(i).equals("[]")) {
//                System.out.println(Selectnews.list6.get(i));
//            } else {
//                JSONArray jsonObject = JSONArray.parseArray(Selectnews.list6.get(i));
//                JSONObject job = jsonObject.getJSONObject(0);
//                System.out.println(job.get("content").toString().trim());
//            }
//
//        }
//    }
//
//    @GetMapping(value = "/getky6")
//    public void getkn() {
//        getky();
//    }
//
//    public void getky2(String type_name) {
//        System.out.println("输出");
//        System.out.println(Selectnews.list1.size());
//        try {
//            for (int i = 0; i < Selectnews.list1.size(); i++) {
//                Book book = new Book();
//                Bookmain bookmain = new Bookmain();
//                book.setAuthor(Selectnews.list2.get(i));
//                book.setType(type_name);
//                book.setName(Selectnews.list1.get(i));
//                if (Selectnews.list3.get(i) != null) {
//                    book.setPrice(new BigDecimal(Selectnews.list3.get(i).substring(1)));
//                    bookmain.setPrice(new BigDecimal(Selectnews.list3.get(i).substring(1)));
//                } else {
//                    book.setPrice(new BigDecimal("12"));
//                    bookmain.setPrice(new BigDecimal("12"));
//                }
//                if (Selectnews.list6.get(i).equals("[]")) {
//                    book.setIntroduction(null);
//                } else {
//                    JSONArray jsonObject = JSONArray.parseArray(Selectnews.list6.get(i));
//                    JSONObject job = jsonObject.getJSONObject(0);
//                    book.setIntroduction(job.get("content").toString().trim());
//                }
//                bookService.save(book);
//                bookmain.setTranslator(Selectnews.list7.get(i));
//                bookmain.setAuthor(Selectnews.list2.get(i));
//                bookmain.setName(Selectnews.list1.get(i));
//                bookmain.setData(Selectnews.list4.get(i));
//                bookmain.setPublish(Selectnews.list8.get(i));
//                bookmain.setPic(Selectnews.list9.get(i));
//                bookmainService.save(bookmain);
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        } finally {
//            Selectnews.list1.clear();
//            Selectnews.list2.clear();
//            Selectnews.list3.clear();
//            Selectnews.list4.clear();
//            Selectnews.list5.clear();
//            Selectnews.list6.clear();
//            Selectnews.list7.clear();
//            Selectnews.detil.clear();
//        }
//    }
//
//    public void startKt(String url) {
////        String url = "http://www.qdaily.com/";
//        Spider.create(new Selectnews())
//                .setDownloader(new HttpClientDownloader())
//                .addUrl(url)
//                //开启5个线程抓取
//                //启动爬虫
//                .start();
//    }
}
