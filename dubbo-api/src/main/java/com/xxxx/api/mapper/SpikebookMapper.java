package com.xxxx.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.api.entity.Spikebook;
import com.xxxx.api.entity.dto.BookVO;
import com.xxxx.api.entity.dto.spikebookDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-30
 */
public interface SpikebookMapper extends BaseMapper<Spikebook> {
    @Select("SELECT\n" +
            "\tm_book.`name`,\n" +
            "\tm_bookmain.pic,\n" +
            "\tm_book.price,\n" +
            "\tm_spikebook.size,\n" +
            "\tm_spikebook.nowsize,\n" +
            "\tm_spikebook.`start`,\n" +
            "\tm_spikebook.`end`,\n" +
            "\tm_book.author,\n" +
            "\tm_bookmain.publish \n" +
            "FROM\n" +
            "\tm_book,\n" +
            "\tm_spikebook,\n" +
            "\tm_bookmain \n" +
            "WHERE\n" +
            "\tm_book.`name` = m_bookmain.`name` \n" +
            "\tAND m_book.`name` = m_spikebook.bookname ;")
    List<spikebookDto> getSpike_Book();

    @Update("truncate table m_spikebook")
    void deleteUser();
}
