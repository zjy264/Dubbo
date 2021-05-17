package com.xxxx.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xxxx.api.entity.Shopping;
import com.xxxx.api.entity.dto.ShopDto;
import com.xxxx.api.mapper.ShoppingMapper;
import com.xxxx.api.service.ShoppingService;
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
@Service(interfaceClass = ShoppingService.class)
@Component
public class ShoppingServiceImpl extends ServiceImpl<ShoppingMapper, Shopping> implements ShoppingService {

    @Override
    public List<ShopDto> getBookShopping(String username) {
        return this.baseMapper.getBookShopping(username);
    }

    @Override
    public List<ShopDto> getBookShopping_no_buy(String username) {
        return this.baseMapper.getBookShopping_no_buy(username);
    }
}
