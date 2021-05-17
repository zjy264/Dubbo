package com.xxxx.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.api.entity.Role;
import com.xxxx.api.mapper.RoleMapper;
import com.xxxx.api.service.RoleService;
import com.xxxx.api.uilt.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired(required = false)
    private RoleMapper roleMapper;
    @Reference(interfaceClass = RoleService.class)
    private RoleService roleService;

    @PostMapping("/getRoles")
    public Result getRoles(){
        Result result=new Result();
        List<Role> roles=roleMapper.selectList(null);
        result.setCode("200");
        result.setData(roles);
        return result;
    }

    @PostMapping("/addRole")
    public Result addRoles(@RequestBody Role role){
        Result result=new Result();
        QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("role",role.getRole());
        roleService.saveOrUpdate(role,queryWrapper);
        List<Role> roles=roleMapper.selectList(null);
        result.setCode("200");
        result.setData(roles);
        return result;
    }
}
