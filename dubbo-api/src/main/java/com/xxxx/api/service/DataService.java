package com.xxxx.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.api.entity.Book;
import com.xxxx.api.entity.Bookmain;
import com.xxxx.api.entity.dto.BookVO;

import java.util.List;

public interface DataService {
        void SaveBookData(List<? extends BookVO> page,String name);
        List<BookVO> getBookData(String name);
        List<Book> getBookData_s(String name);
        List<Bookmain> getBookData_T(String name);
        Boolean checkData(String name);
}
