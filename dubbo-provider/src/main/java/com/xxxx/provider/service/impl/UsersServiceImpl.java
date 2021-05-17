package com.xxxx.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxxx.api.entity.Users;
import com.xxxx.api.entity.dto.LoginToken;
import com.xxxx.api.mapper.UsersMapper;
import com.xxxx.api.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
@Service(interfaceClass = UsersService.class)
@Component
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Autowired(required = false)
    private RedisTemplate redisTemplate;
    @Override
    public Users selectUserById(Integer id) {
        Users users=new Users();
        users.setId(2);
        users.setUsername("zjy");
        users.setRole("ROLE_ADMIN");
        System.out.println("消费成功，用户："+users);
        return users;
    }

    @Override
    public Users selectUSerByName_zjy(String username) {
        return  this.baseMapper.selectByName(username);
    }

}
