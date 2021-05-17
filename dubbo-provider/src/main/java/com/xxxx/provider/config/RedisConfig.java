package com.xxxx.provider.config;

import com.xxxx.api.entity.dto.BookVO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

/**
 * @ClassName RedisConfig
 * @Deacription TODO
 * @Author zjy
 * @Date 2021/4/23 10:33
 * @Version 2.0
 **/
@Configuration
public class RedisConfig {

    // 编写我们自己的 redisTemplate
    @Bean
    public RedisTemplate<String,Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory){
        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return redisTemplate;
    }
    @Bean
    public RedisTemplate<String,List<BookVO>> redisListTemplate(LettuceConnectionFactory lettuceConnectionFactory){
        RedisTemplate<String,List<BookVO>> redisTemplate=new RedisTemplate<>();
        redisTemplate.setValueSerializer(new GenericToStringSerializer<BookVO>(BookVO.class));
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return redisTemplate;
    }
}