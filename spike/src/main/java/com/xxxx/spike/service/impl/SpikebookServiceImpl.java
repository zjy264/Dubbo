package com.xxxx.spike.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.api.entity.Spikebook;
import com.xxxx.api.entity.dto.spikebookDto;
import com.xxxx.api.mapper.SpikeMapper;
import com.xxxx.api.mapper.SpikebookMapper;
import com.xxxx.api.service.SpikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.api.service.SpikebookService;
import com.xxxx.spike.uilt.RedisClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-30
 */
@Service(interfaceClass = SpikebookService.class)
@Component
public class SpikebookServiceImpl extends ServiceImpl<SpikebookMapper, Spikebook> implements SpikebookService {

    private static final String HAS_BUY_USER_KEY = "HAS_BUY_USER_KEY_";

    private static final String LOCK_KEY = "LOCK_KEY_";

    private static final String FAIL_BUYED = "已经买过了";
    private static final String BUYE_SUCCESS = "抢到了，订单生成中";
    private static final String FAIL_SOLD_OUT = "没货了";
    private static final String FAIL_BUSY = "排队中，请重试！";

    @Autowired(required = false)
    private RedisTemplate redisTemplate;
    @Autowired(required = false)
    private RedisClient redisClient;
    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    @Autowired(required = false)
    private SpikebookMapper spikebookMapper;

    @Override
    public void setSpikeBook() {
        ListOperations listOperations = redisTemplate.opsForList();
        redisTemplate.delete(HAS_BUY_USER_KEY);
        System.out.println("初始化用户抢购表");
        List<spikebookDto> list2 = getSpike_Book();
        listOperations.trim("spike_book", 0, 0);
        listOperations.leftPop("spike_book");
        for (int i = 0; i < list2.size(); i++) {
            listOperations.leftPush("spike_book", list2.get(i));
            System.out.println(list2.get(i).getName() + "商品已经初始化属性完成。");
        }

    }

    @Override
    public String insertOrder(String username, String bookname) {
        ListOperations listOperations = redisTemplate.opsForList();
        List<String> spike_user = listOperations.range(HAS_BUY_USER_KEY + bookname, 0, -1);
        for (int p = 0; p < spike_user.size(); p++) {
            if (spike_user.get(p).equals(username)) {
                System.out.println("已经买过了");
                return FAIL_BUYED;//"已经买过了"
            }
        }
        int redisExpireTime = 1 * 1000;
        long lockValue = System.currentTimeMillis() + redisExpireTime;
        boolean getLock = redisClient.lock(LOCK_KEY, String.valueOf(lockValue));
//        System.out.println(username + " getLock:" + getLock);
        long index = 0;
        if (getLock) {
            spikebookDto spike = null;
            List<spikebookDto> spikelist = listOperations.range("spike_book", 0, -1);
            for (int i = 0; i < spikelist.size(); i++) {
                if (spikelist.get(i).getName().equals(bookname)) {
                    spike = spikelist.get(i);
                    index = i;
                }
            }
            System.out.println(spike);
            System.out.println(bookname + "--库存还有:" + spike.getNowsize());
            //库存大于0才能继续下单
            if (spike.getNowsize() > 0) {
                int size = spike.getNowsize() - 1;
                spike.setNowsize(size);
//                HashMap<String,String> hashMap=new HashMap<>();
//                hashMap.put("username",username);
//                hashMap.put("bookname",bookname);
//                hashMap.put("nowsize",String.valueOf(size));
                //执行入库操作
//                rabbitTemplate.convertAndSend("spike_topicExchange","spike.msg",hashMap);//入库
//                spikebook.setName(bookname);
//                spikebook.setNowsize(size);
//                spikebook.set;
                //商品数减1
                listOperations.set("spike_book", index, spike);
                //记录用户已买
                listOperations.leftPush(HAS_BUY_USER_KEY + bookname, username);
//                hashOperations.put(HAS_BUY_USER_KEY,bookname,username);
                //手动释放锁
                redisClient.unlock(LOCK_KEY, String.valueOf(lockValue));
                return BUYE_SUCCESS;//"抢到了，订单生成中"
            } else {
                System.out.println("亲，" + FAIL_SOLD_OUT);
                //手动释放锁
                redisClient.unlock(LOCK_KEY, String.valueOf(lockValue));
                return FAIL_SOLD_OUT;//"没货了"
            }
        } else {
            return FAIL_BUSY;//"排队中，请重试！"
        }
    }

    @Override
    public List<spikebookDto> getSpike_Book() {
        return this.baseMapper.getSpike_Book();
    }

    @Override
    public List<spikebookDto> getSpike_Book_Main(LocalDateTime middle) {
        ListOperations listOperations = redisTemplate.opsForList();
        List<spikebookDto> list = listOperations.range("spike_book", 0, -1);
        List<spikebookDto> list2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStart().isBefore(middle)) {
                if (list.get(i).getEnd().isAfter(middle)) {
                    list2.add(list.get(i));
                }
            }
        }
        return list2;
    }


}
