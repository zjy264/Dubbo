package com.xxxx.api.mapper;

import com.xxxx.api.entity.Users;
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
public interface UsersMapper extends BaseMapper<Users> {
    @Select("SELECT * FROM m_users where username = #{username}")
    Users selectByName(@Param("username") String username);
}
