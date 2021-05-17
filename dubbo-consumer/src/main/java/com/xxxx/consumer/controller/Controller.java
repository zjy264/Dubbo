package com.xxxx.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.api.entity.Users;
import com.xxxx.api.entity.dto.BookVO2;
import com.xxxx.api.mapper.UsersMapper;
import com.xxxx.api.service.BookService;
import com.xxxx.api.service.BookmainService;
import com.xxxx.api.uilt.BaiduTongjiUtil;
import com.xxxx.api.uilt.Result;
import com.xxxx.api.uilt.Selectnews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后端页面控制器
 * </p>
 *
 * @author write by zjy
 * @since 2020-11-29
 */
@RestController
@RequestMapping("/controller")
public class Controller {

    @Reference(interfaceClass = BookService.class)
    private BookService bookService;
    @Reference(interfaceClass = BookmainService.class)
    private BookmainService bookmainService;
    @Autowired(required = false)
    private UsersMapper usersMapper;
    @Autowired(required = false)
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/test")
    public Result checkRole() {
        Result result = new Result();
        result.setCode("200");
        result.setMsg("管理员");
        result.setData(null);
        return result;
    }

    @RequestMapping("/findbooks")
    public Result findbooks(@RequestBody Map<String, String> findbook) {
        Selectnews.detil.clear();
        Selectnews.bookmainList.clear();
        Selectnews.bookList.clear();
        Spider.create(new Selectnews(findbook.get("url"), findbook.get("xpath")))
                .setDownloader(new HttpClientDownloader())
                .addUrl(findbook.get("url"))
                //开启5个线程抓取
                //启动爬虫
                .start();
        Result result = new Result();
        result.setCode("100");
        result.setMsg("爬取成功");
        result.setData(Selectnews.detil);
        return result;
    }

    @RequestMapping("/getbooks")
    public Result getbooks() {
        Result result = new Result();
        result.setCode("200");
        result.setMsg("获取成功");
        result.setData(Selectnews.detil);
        return result;
    }

    @RequestMapping("/getmainbook")
    public Result getmainbook(@RequestBody BookVO2 bookVO2) throws InterruptedException {
        for (int i = 0; i < Selectnews.detil.size(); i++) {
            System.out.println("开始第" + i + "个任务");
            Thread.sleep(200);
            Spider.create(new Selectnews(bookVO2.getName(), bookVO2.getAuthor(), bookVO2.getPublish(), bookVO2.getTranslator(), bookVO2.getIntroduction(), bookVO2.getData(), bookVO2.getPrice(), bookVO2.getPic(), bookVO2.getType()))
                    .setDownloader(new HttpClientDownloader())
                    .addUrl(Selectnews.detil.get(i))
                    //开启5个线程抓取
                    //启动爬虫
                    .start();
        }
//        String url="http://item.xhsd.com/items/110000100268513";
//        Spider.create(new Selectnews(bookVO2.getName(),bookVO2.getAuthor(),bookVO2.getPublish(),bookVO2.getTranslator(),bookVO2.getIntroduction(),bookVO2.getData(),bookVO2.getPrice(),bookVO2.getPic(),bookVO2.getType()))
//                .setDownloader(new HttpClientDownloader())
//                .addUrl(url)
//                //开启5个线程抓取
//                //启动爬虫
//                .start();
        Result result = new Result();
        result.setCode("200");
        result.setMsg("获取成功");
        result.setData(new List[]{Selectnews.bookList, Selectnews.bookmainList});
        return result;
    }

    @RequestMapping("/getAllbooks")
    public Result getAllbooks() {
        Result result = new Result();
        result.setCode("200");
        result.setMsg("查看成功");
        result.setData(new List[]{Selectnews.bookList, Selectnews.bookmainList});
        return result;
    }

    @RequestMapping("/saveAllbooks")
    public Result saveAllbooks() {
        Result result = new Result();
        result.setCode("200");
        result.setMsg("查看成功");
        bookmainService.saveBatch(Selectnews.bookmainList);
        bookService.saveBatch(Selectnews.bookList);
        result.setData(new List[]{Selectnews.bookList, Selectnews.bookmainList});
        return result;
    }

    @PostMapping("/getUsers")
    public Result getUsers(){
        Result result=new Result();
        Users users=new Users();
        QueryWrapper<Users> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("id","username","nike","role","status");
        List<Users> list=usersMapper.selectList(queryWrapper);
        result.setMsg("成功");
        result.setCode("200");
        result.setData(list);
        return result;
    }

    @PostMapping("/SetUserStatus")
    public Result SetUserStatus(@RequestBody Users users){
        Result result=new Result();
        QueryWrapper<Users> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",users.getUsername());
        int updata_ok =usersMapper.update(users,queryWrapper);
        result.setMsg("成功");
        result.setCode("200");
        result.setData(updata_ok);
        return result;
    }

    @PostMapping("/GetBaiduData")
    public Result GetBaiduData(@RequestBody JSONObject obj){
        Result result=new Result();
        Object data=new BaiduTongjiUtil().getData(obj);
        result.setMsg("成功");
        result.setCode("200");
        JSONObject json = (JSONObject) JSON.toJSON(data);
        JSONObject jsonObject=json.getJSONObject("body").getJSONArray("data").getJSONObject(0).getJSONObject("result");
        JSONArray fields=jsonObject.getJSONArray("fields");//类型名称
        JSONArray items=jsonObject.getJSONArray("items");
        List<String> list1=new ArrayList<>();
        list1.add("浏览量");
        list1.add("游客量");
        list1.add("ip量");
        list1.add("离开率");
        list1.add("平均访问时长");
        list1.add("转化次数");
        List<List<String>> lists=new ArrayList<>();
        List<String> x_data=new ArrayList<>();//日期
        List<String> x_pv=new ArrayList<>();//浏览量
        List<String> x_yk=new ArrayList<>();//游客量
        List<String> x_ip=new ArrayList<>();//IP量
        List<String> x_out=new ArrayList<>();//离开率
        List<String> x_time=new ArrayList<>();//平均访问时长
        List<String> x_zhuan=new ArrayList<>();//转化次数
        lists.add(x_pv);
        lists.add(x_yk);
        lists.add(x_ip);
        lists.add(x_out);
        lists.add(x_time);
        lists.add(x_zhuan);
        for (int i=0;i<items.getJSONArray(0).size();i++){
            x_data.add(items.getJSONArray(0).getJSONArray(i).get(0).toString());//日期
            for (int j=1;j<fields.size();j++){
                if (items.getJSONArray(1).getJSONArray(i).get(j-1).equals("--")){
                    lists.get(j-1).add("0");
                }else {
                    lists.get(j-1).add(items.getJSONArray(1).getJSONArray(i).get(j-1).toString());
                }
            }
        }
        lists.add(x_data);
        lists.add(list1);
        result.setData(lists);
        return result;
    }
}
