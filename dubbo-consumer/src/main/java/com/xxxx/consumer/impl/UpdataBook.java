package com.xxxx.consumer.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xxxx.api.entity.dto.BookVO;
import com.xxxx.api.mapper.BookMapper;
import com.xxxx.api.service.BookService;
import com.xxxx.api.service.DataService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@RabbitListener(queues = "book")
@Component
public class UpdataBook {
    @Autowired(required = false)
    private BookMapper bookMapper;
    @Reference(interfaceClass = BookService.class)
    private BookService bookService;

    @Reference(interfaceClass = DataService.class)
    private DataService dataService;

    @RabbitHandler
    public void updataBook(HashMap<String,List<? extends BookVO>> hashMap){
        System.out.println("执行book队列");
        String name="";
        for(String key : hashMap.keySet()){
            dataService.SaveBookData(hashMap.get(key),key);
            name=key;
        }
        System.out.println("更新"+name+"数据");
    }
}
