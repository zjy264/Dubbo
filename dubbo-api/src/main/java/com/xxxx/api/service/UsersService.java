package com.xxxx.api.service;

import com.xxxx.api.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.api.entity.dto.LoginToken;
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
public interface UsersService extends IService<Users> {
    Users selectUserById(Integer id);

//    void saveToken(LoginToken loginToken);

    @Select("SELECT * FROM m_users where username = #{username}")
    Users selectUSerByName_zjy(@Param("username") String username);
}
