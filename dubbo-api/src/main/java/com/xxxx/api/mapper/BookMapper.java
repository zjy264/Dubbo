package com.xxxx.api.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.api.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.api.entity.dto.BookVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
public interface BookMapper extends BaseMapper<Book> {
    @Select("SELECT m_book.*,m_bookmain.`publish`,m_bookmain.`data`,m_bookmain.`pic`,m_bookmain.`translator` FROM m_book,m_bookmain WHERE m_book.name=m_bookmain.name")
    List<BookVO> getBook(Page page);
    @Select("SELECT m_book.*,m_bookmain.`publish`,m_bookmain.`data`,m_bookmain.`pic`,m_bookmain.`translator` FROM m_book,m_bookmain WHERE m_book.name=m_bookmain.name ORDER BY zan DESC")
    List<BookVO> getBook_Liulan(Page page);
    @Select("SELECT m_book.*,m_bookmain.`publish`,m_bookmain.`data`,m_bookmain.`pic`,m_bookmain.`translator` FROM m_book,m_bookmain,m_collect WHERE m_book.name=m_bookmain.name and m_book.`name`=m_collect.mycollect")
    List<BookVO> getBook_Collect(Page page);
}
