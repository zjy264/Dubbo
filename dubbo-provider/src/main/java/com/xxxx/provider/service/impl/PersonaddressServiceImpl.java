package com.xxxx.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxxx.api.entity.Personaddress;
import com.xxxx.api.mapper.PersonaddressMapper;
import com.xxxx.api.service.PersonaddressService;
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
@Service(interfaceClass = PersonaddressService.class)
@Component
public class PersonaddressServiceImpl extends ServiceImpl<PersonaddressMapper, Personaddress> implements PersonaddressService {

}
