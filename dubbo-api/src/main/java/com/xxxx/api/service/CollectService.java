package com.xxxx.api.service;

import com.xxxx.api.entity.Collect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.api.entity.dto.BookVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
public interface CollectService extends IService<Collect> {
    @Select("SELECT m_bookmain.* FROM m_collect,m_bookmain WHERE m_collect.mycollect=m_bookmain.name AND m_collect.username=#{username}")
    List<BookVO> getBookCollection(@Param("username") String username);
}
