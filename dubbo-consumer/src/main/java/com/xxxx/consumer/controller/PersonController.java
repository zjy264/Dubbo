package com.xxxx.consumer.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.api.entity.Person;
import com.xxxx.api.entity.Users;
import com.xxxx.api.mapper.PersonMapper;
import com.xxxx.api.mapper.UsersMapper;
import com.xxxx.api.uilt.QiniuCloudUtil;
import com.xxxx.api.uilt.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired(required = false)
    private PersonMapper personMapper;

    @Autowired(required = false)
    private UsersMapper usersMapper;
    @PostMapping("/getPerson")
    public Result getperson(@RequestBody Map<String, String> getPerson) {
        Result result = new Result();
        Person person = personMapper.selectByName(getPerson.get("username"));
        if (person==null){
            result.setMsg("没有找到person");
            result.setCode("400");
            result.setData(null);
        }else{
            result.setMsg("找到了person");
            result.setCode("200");
            result.setData(person);
        }
        return result;
    }
    @RequestMapping("/updataPerson")
    public Result updataPerson(@RequestBody Person person) {
        Result result = new Result();
        Users users=new Users();
        users.setNike(person.getNike());
        QueryWrapper<Users> queryWrapper2=new QueryWrapper<>();
        queryWrapper2.eq("username",person.getUsername());
        usersMapper.update(users,queryWrapper2);
        QueryWrapper<Person> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", person.getUsername());
        int updata = personMapper.update(person, queryWrapper);
        result.setCode("200");
        result.setData(person);
        result.setMsg(String.valueOf(updata));
        return result;
    }

    @RequestMapping("/upload_image")
    public Result upload_image(MultipartFile file) throws IOException {
        Result result = new Result();
        new QiniuCloudUtil().uploadimage(file);
        String fileName = file.getOriginalFilename();
        result.setCode("200");
        return result;
    }
}
