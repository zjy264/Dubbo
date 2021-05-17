package com.xxxx.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxxx.api.entity.Collect;
import com.xxxx.api.entity.dto.BookVO;
import com.xxxx.api.mapper.CollectMapper;
import com.xxxx.api.service.CollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
@Service(interfaceClass = CollectService.class)
@Component
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Override
    public List<BookVO> getBookCollection(String username) {
        return this.baseMapper.getBookCollection(username);
    }
}
