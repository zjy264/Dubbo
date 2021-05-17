package com.xxxx.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxxx.api.entity.Role;
import com.xxxx.api.mapper.RoleMapper;
import com.xxxx.api.service.RoleService;
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
@Service(interfaceClass = RoleService.class)
@Component
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
