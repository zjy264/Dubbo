package com.xxxx.api.mapper;

import com.xxxx.api.entity.Person;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
public interface PersonMapper extends BaseMapper<Person> {
    @Select("SELECT * FROM m_person where username = #{username}")
    Person selectByName(@Param("username") String username);
}
