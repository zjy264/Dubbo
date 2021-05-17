package com.xxxx.spike.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxxx.api.entity.Spike;
import com.xxxx.api.entity.Spikebook;
import com.xxxx.api.mapper.SpikeMapper;
import com.xxxx.api.service.SpikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * @since 2021-04-30
 */
@Service(interfaceClass = SpikeService.class)
@Component
public class SpikeServiceImpl extends ServiceImpl<SpikeMapper, Spike> implements SpikeService {

}
