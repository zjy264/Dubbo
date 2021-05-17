package com.xxxx.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxxx.api.entity.dto.LoginToken;
import com.xxxx.api.service.UserTokenService;
import com.xxxx.provider.uilt.SerializeUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Service(interfaceClass = UserTokenService.class)
@Component
public class UserTokenServiceImpl implements UserTokenService {
    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;
    @Autowired(required = false)
    private RedisTemplate redisTemplate;
    @Override
    public void saveToken(LoginToken loginToken) {
        try{
            System.out.println("准备存储token");
            ValueOperations operations = redisTemplate.opsForValue();
            operations.set(loginToken.getName(), loginToken);
            System.out.println("存储token成功");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public LoginToken getToken(String username) {
        ValueOperations operations=redisTemplate.opsForValue();
        if (redisTemplate.hasKey(username)){
            LoginToken loginToken= (LoginToken) operations.get(username);
            return loginToken;
        }else {
            return null;
        }

    }

}
