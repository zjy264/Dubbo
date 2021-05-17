package com.xxxx.api.mapper;

import com.xxxx.api.entity.Collect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.api.entity.dto.BookVO;
import org.apache.ibatis.annotations.Param;
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
public interface CollectMapper extends BaseMapper<Collect> {
    @Select("SELECT m_bookmain.* FROM m_collect,m_bookmain WHERE m_collect.mycollect=m_bookmain.name AND m_collect.username=#{username}")
    List<BookVO> getBookCollection(@Param("username") String username);
}
