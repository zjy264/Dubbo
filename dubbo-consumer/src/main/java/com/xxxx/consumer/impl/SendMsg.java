package com.xxxx.consumer.impl;

import com.alibaba.dubbo.config.annotation.Reference;

import com.xxxx.api.uilt.SendMessageUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@RabbitListener(queues = "msg")
@Component
public class SendMsg {

    @RabbitHandler
    public void SendMsg(HashMap<String,String> hashMap){
        try {
            System.out.println("yzm:"+hashMap.get("yzm"));
            System.out.println("time:"+hashMap.get("time"));
            String[] templateParams={hashMap.get("yzm"),"5"};
            new SendMessageUtil().sendMsg(hashMap.get("phone") ,templateParams);
            System.out.println("发送短信成功");
        }catch (Exception e){
            System.out.println("发送短信失败，错误原因：");
            System.out.println(e.getMessage());
        }

    }
}