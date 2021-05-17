package com.xxxx.consumer.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.api.entity.Book;
import com.xxxx.api.entity.dto.BookVO;
import com.xxxx.api.mapper.BookMapper;
import com.xxxx.api.service.BookService;
import com.xxxx.api.service.DataService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@RabbitListener(queues = "person")
@Component
public class UpdataUser {

    @Autowired(required = false)
    private BookMapper bookMapper;
    @Reference(interfaceClass = BookService.class)
    private BookService bookService;

    @Reference(interfaceClass = DataService.class)
    private DataService dataService;

    @RabbitHandler
    public void updataUser(List<BookVO> page){
        System.out.println("执行队列person");
//        dataService.SaveBookData(page);
        System.out.println("更新数据");
    }
}
