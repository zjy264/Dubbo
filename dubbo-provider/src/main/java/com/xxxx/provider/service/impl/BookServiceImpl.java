package com.xxxx.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.api.entity.Book;
import com.xxxx.api.entity.dto.BookVO;
import com.xxxx.api.mapper.BookMapper;
import com.xxxx.api.service.BookService;
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
@Service(interfaceClass = BookService.class)
@Component
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Override
    public Book selectBookById(Integer id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<BookVO> getBook(Page page) {
        return this.baseMapper.getBook(page);
    }

    @Override
    public List<BookVO> getBook_Liulan(Page page) {
        return this.baseMapper.getBook_Liulan(page);
    }

    @Override
    public List<BookVO> getBook_Collect(Page page) {
        return this.baseMapper.getBook_Collect(page);
    }
}
