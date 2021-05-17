package com.xxxx.spike.uilt;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisClient {
    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    public boolean lock(String key, String value) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            //加锁成功就返回true
            return true;
        }
        //不加下面这个可能出现死锁情况
        //value为当前时间+超时时间
        //获取上一个锁的时间,并判断是否小于当前时间,小于就下一步判断,就返回true加锁成功
        //currentValue=A 这两个线程的value都是B 其中一个线程拿到锁
        String currentValue = (String) redisTemplate.opsForValue().get(key);
        //如果锁过期
        if (!StringUtils.isEmpty(currentValue)
                && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //存储时间要小于当前时间
            //出现死锁的另一种情况,当多个线程进来后都没有返回true,接着往下执行,执行代码有先后,而if判断里只有一个线程才能满足条件
            //oldValue=currentValue
            //多个线程进来后只有其中一个线程能拿到锁(即oldValue=currentValue),其他的返回false
            //获取上一个锁的时间
            String oldValue = (String) redisTemplate.opsForValue().getAndSet(key, value);
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                return true;
            }

        }
        return false;
    }

    public void unlock(String key, String value) {
        //执行删除可能出现异常需要捕获
        try {
            String currentValue = (String) redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                //如果不为空,就删除锁
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            System.out.println("[redis分布式锁] 解锁"+ e);
        }
    }
}
