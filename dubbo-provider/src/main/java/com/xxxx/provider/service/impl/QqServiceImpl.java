package com.xxxx.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxxx.api.entity.Qq;
import com.xxxx.api.mapper.QqMapper;
import com.xxxx.api.service.QqService;
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
@Service(interfaceClass = QqService.class)
@Component
public class QqServiceImpl extends ServiceImpl<QqMapper, Qq> implements QqService {

}
