package com.xxxx.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxxx.api.entity.Person;
import com.xxxx.api.mapper.PersonMapper;
import com.xxxx.api.service.PersonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
@Service(interfaceClass = PersonService.class)
@Component
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService {
    @Override
    public Person selectByName(String username) {
        return this.baseMapper.selectByName(username);
    }
}
