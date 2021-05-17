package com.xxxx.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.api.entity.Book;
import com.xxxx.api.entity.Bookmain;
import com.xxxx.api.entity.dto.BookVO;
import com.xxxx.api.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = DataService.class)
@Component
public class DataServiceImpl implements DataService {
    @Autowired(required = false)
    private RedisTemplate redisTemplate;
    @Override
    public void SaveBookData(List<? extends BookVO> page,String name) {
        ListOperations<String, BookVO> listOperations=redisTemplate.opsForList();
        listOperations.trim(name,0,0);
        //将list中的剩余的一个值也删除
        listOperations.leftPop(name);
        for (int i=0;i<page.size();i++){
            listOperations.leftPush(name,page.get(i));
        }
        System.out.println("更新book数据");
    }

    @Override
    public List<BookVO> getBookData(String name) {
        ListOperations<String, BookVO> listOperations=redisTemplate.opsForList();
        return listOperations.range(name,0,-1);
    }

    @Override
    public List<Book> getBookData_s(String name) {
        ListOperations<String, Book> listOperations=redisTemplate.opsForList();
        return listOperations.range(name,0,-1);
    }

    @Override
    public List<Bookmain> getBookData_T(String name) {
        ListOperations<String,Bookmain> listOperations=redisTemplate.opsForList();
        return listOperations.range(name,0,-1);
    }

    @Override
    public Boolean checkData(String name) {
        if (redisTemplate.hasKey(name)){
            System.out.println("检查成功，redis中有"+name+"的数据。");
            return true;
        }
        return false;
    }
}
