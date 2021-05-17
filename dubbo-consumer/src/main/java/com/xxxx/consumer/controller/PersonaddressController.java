package com.xxxx.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.api.entity.Personaddress;
import com.xxxx.api.mapper.PersonaddressMapper;
import com.xxxx.api.service.PersonaddressService;
import com.xxxx.api.uilt.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
@RestController
@RequestMapping("/personaddress")
public class PersonaddressController {
    @Autowired(required = false)
    private PersonaddressMapper personAddressMapper;
    @Reference(interfaceClass = PersonaddressService.class)
    private PersonaddressService personAddressService;

    @PostMapping("/getPersonAddress")
    public Result getPersonAddress(@RequestBody Map<String,String> shop){
        QueryWrapper<Personaddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",shop.get("username"));
        queryWrapper.orderByDesc("defaultaddress");
//        new PersonAddress().set
        List<Personaddress> address = personAddressMapper.selectList(queryWrapper);
        Result result=new Result();
        result.setCode("200");
        result.setData(address);
        return result;
    }
    @PostMapping("/UpdataPersonAddress")
    public Result updataPersonAddress(@RequestBody Personaddress personAddress){
        personAddressService.updateById(personAddress);
        Result result=new Result();
        result.setCode("200");
        result.setData(personAddress);
        return result;
    }
    @PostMapping("/AddPersonAddress")
    public Result addPersonAddress(@RequestBody Personaddress personAddress){
//        personAddressService.updateById(personAddress);
        personAddressService.save(personAddress);
        Result result=new Result();
        result.setCode("200");
        result.setData(personAddress);
        return result;
    }

    @PostMapping("/DeletePersonAddress")
    public Result deletePersonAddress(@RequestBody Personaddress personAddress){
//        personAddressService.updateById(personAddress);
//        personAddressService.save(personAddress);
        personAddressService.removeById(personAddress.getId());
        Result result=new Result();
        result.setCode("200");
        result.setData(personAddress);
        return result;
    }
}
