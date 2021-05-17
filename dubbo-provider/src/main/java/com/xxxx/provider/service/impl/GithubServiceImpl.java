package com.xxxx.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxxx.api.entity.Github;
import com.xxxx.api.mapper.GithubMapper;
import com.xxxx.api.service.GithubService;
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
@Service(interfaceClass = GithubService.class)
@Component
public class GithubServiceImpl extends ServiceImpl<GithubMapper, Github> implements GithubService {

}
