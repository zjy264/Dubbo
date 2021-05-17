package com.xxxx.api.uilt;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xxxx.api.entity.Book;
import com.xxxx.api.entity.Bookmain;
import lombok.Data;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Selectnews implements PageProcessor {
    public static int sum = 0;
    public static List<String> detil = new ArrayList<String>();
    public static List<String> list1 = new ArrayList<String>();
    public static List<String> list2 = new ArrayList<String>();
    public static List<String> list3 = new ArrayList<String>();
    public static List<LocalDate> list4 = new ArrayList<LocalDate>();
    public static List<String> list5 = new ArrayList<String>();
    public static List<String> list6 = new ArrayList<String>();
    public static List<String> list7 = new ArrayList<String>();
    public static List<String> list8 = new ArrayList<String>();
    public static List<String> list9 = new ArrayList<String>();
    public static List<Book> bookList = new ArrayList<>();
    public static List<Bookmain> bookmainList = new ArrayList<>();
    private String url;

    private String xpath;

    private String name;

    private String author;

    private String publish;

    private String translator;

    private String introduction;

    private String data;

    private String price;

    private String pic;

    private String type;

    public Selectnews(String url, String xpath) {
        this.url = url;
        this.xpath = xpath;
    }

    public Selectnews(String name, String author, String publish, String translator, String introduction, String data, String price, String pic, String type) {
        this.name = name;
        this.author = author;
        this.publish = publish;
        this.translator = translator;
        this.introduction = introduction;
        this.data = data;
        this.price = price;
        this.pic = pic;
        this.type = type;
    }

    public Selectnews() {

    }

    @Override
    public void process(Page page) {
        if (detil.size() == 0) {
            try {
                String books1 = getXpath().substring(0, getXpath().indexOf("?"));
                String books2 = getXpath().substring(getXpath().indexOf("?") + 1);
                for (int i = 1; ; i++) {
                    if (page.getHtml().xpath(books1 + i + books2).toString() == null) {
                        break;
                    }
                    String detals = "http:" + page.getHtml().xpath(books1 + i + books2).toString();
                    detil.add(detals);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            Book book = new Book();
            Bookmain bookmain = new Bookmain();
            JSONArray introduction = JSONObject.parseArray(page.getHtml().xpath(getIntroduction()).toString());
            String introductions = "无";
            String data = page.getHtml().xpath(getData()).toString();
            String bookname = page.getHtml().xpath(getName()).toString();
            String author = page.getHtml().xpath(getAuthor()).toString();
            String price = page.getHtml().xpath(getPrice()).toString();
            String pic = page.getHtml().xpath(getPic()).toString();
            String publish = page.getHtml().xpath(getPublish()).toString();
            String translator = page.getHtml().xpath(getTranslator()).toString();
            System.out.println("-------------------");
            System.out.println("第" + sum + "本书");
            System.out.println("书名" + bookname);
            System.out.println("作者" + author);

            if (introduction.size() != 0) {
                introductions = introduction.getJSONObject(0).getString("content").replaceAll("\r\n|\r|\n", "").trim();
            }
            System.out.println("介绍" + introductions);
            System.out.println("译者" + translator);
            System.out.println("出版日期" + data);
            System.out.println("价格" + price);
            System.out.println("书籍图片" + pic);
            System.out.println("出版社" + publish);
            System.out.println("-------------------");
            sum++;
            if (pic != null) {
                book.setIntroduction(introductions);
                BigDecimal bd = new BigDecimal(price.trim().substring(1));
                book.setPrice(bd);
                book.setType(getType());
                book.setAuthor(author);
                book.setName(bookname);
                bookList.add(book);
                bookmain.setName(bookname);
                bookmain.setAuthor(author);
                bookmain.setPic(pic);
                bookmain.setTranslator(translator);
                bookmain.setPublish(publish);
                if (data != null) {
                    bookmain.setData(LocalDateTime.parse(data));
                } else {
                    bookmain.setData(null);
                }
                bookmain.setPrice(bd);
                bookmainList.add(bookmain);
            }
        }

    }

    @Override
    public Site getSite() {
        return Site.me().setSleepTime(1000).setRetryTimes(3);
    }
}
