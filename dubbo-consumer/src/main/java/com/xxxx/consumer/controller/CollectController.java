package com.xxxx.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.api.entity.Collect;
import com.xxxx.api.mapper.CollectMapper;
import com.xxxx.api.service.CollectService;
import com.xxxx.api.uilt.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/collect")
public class CollectController {
    @Reference(interfaceClass = CollectService.class)
    private CollectService collectService;
    @Autowired(required = false)
    private CollectMapper collectMapper;
    @PostMapping("/getCollection")
    public Result getCollection(@RequestBody Map<String, String> collect) {
        Result result = new Result();
        result.setCode("200");
        result.setData(collectService.getBookCollection(collect.get("username")));
        return result;
    }

    @PostMapping("/addCollection")
    public Result addCollection(@RequestBody Map<String, String> collect) {
        Result result = new Result();
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", collect.get("username"));
        queryWrapper.eq("mycollect", collect.get("bookname"));
        if (collect.get("addorremove").equals("0")) {
            collectService.removeById(collectMapper.selectList(queryWrapper).get(0).getId());
        } else {
            Collect collection = new Collect();
            collection.setMycollect(collect.get("bookname"));
            collection.setUsername(collect.get("username"));
            collectService.save(collection);
        }
        result.setCode("200");
        result.setData(null);
        return result;
    }
}
