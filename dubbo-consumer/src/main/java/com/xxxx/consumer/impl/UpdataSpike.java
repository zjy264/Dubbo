package com.xxxx.consumer.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xxxx.api.entity.Bookmain;
import com.xxxx.api.entity.Spike;
import com.xxxx.api.entity.Spikebook;
import com.xxxx.api.entity.dto.BookVO;
import com.xxxx.api.mapper.SpikeMapper;
import com.xxxx.api.mapper.SpikebookMapper;
import com.xxxx.api.service.SpikeService;
import com.xxxx.api.service.SpikebookService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RabbitListener(queues = "spike")
@Component
public class UpdataSpike {

    @Reference(interfaceClass = SpikebookService.class)
    private SpikebookService spikebookService;
    @Autowired(required = false)
    private SpikebookMapper spikebookMapper;
    @Reference(interfaceClass = SpikeService.class)
    private SpikeService spikeService;
    @Autowired(required = false)
    private SpikeMapper spikeMapper;
    @RabbitHandler
    public void updataSpikebook(HashMap<String,String> hashMap){
        String username=hashMap.get("username");
        String bookname=hashMap.get("bookname");
        int size=Integer.parseInt(hashMap.get("size"));
        System.out.println("执行spike队列");

        System.out.println("更新spike商品");
        LambdaUpdateWrapper<Spikebook> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Spikebook::getBookname, bookname);
        updateWrapper.set(Spikebook::getSize,--size);
        int row=spikebookMapper.update(null, updateWrapper);
        if (row>0){
            System.out.println("更新spike商品数量成功");
        }else {
            System.out.println("更新spike商品数量失败");
        }
        System.out.println("更新spike用户");
        Spike spike=new Spike();
        spike.setUsername(username);
        spike.setBookname(bookname);
        spike.setCreateTime(LocalDateTime.now());
        spikeMapper.insert(spike);
    }
}
