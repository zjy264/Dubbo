package com.xxxx.consumer.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xxxx.api.entity.Users;
import com.xxxx.api.entity.dto.JwtUser;
import com.xxxx.api.service.UsersService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Reference
    private UsersService usersService;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users user = usersService.selectUSerByName_zjy(s);
        return new JwtUser(user);
    }

}
