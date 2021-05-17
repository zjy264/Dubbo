package com.xxxx.api.service;

import com.xxxx.api.entity.Person;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
public interface PersonService extends IService<Person> {
    @Select("SELECT * FROM m_person where username = #{username}")
    Person selectByName(@Param("username") String username);
}
