package com.xxxx.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxxx.api.entity.Bookmain;
import com.xxxx.api.mapper.BookmainMapper;
import com.xxxx.api.service.BookmainService;
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
@Service(interfaceClass = BookmainService.class)
@Component
public class BookmainServiceImpl extends ServiceImpl<BookmainMapper, Bookmain> implements BookmainService {

}
