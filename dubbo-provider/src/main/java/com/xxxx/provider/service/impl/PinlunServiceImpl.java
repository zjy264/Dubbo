package com.xxxx.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxxx.api.entity.Pinlun;
import com.xxxx.api.mapper.PinlunMapper;
import com.xxxx.api.service.PinlunService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
@Service(interfaceClass = PinlunService.class)
@Component
public class PinlunServiceImpl extends ServiceImpl<PinlunMapper, Pinlun> implements PinlunService {

}
