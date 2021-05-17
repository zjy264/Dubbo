package com.xxxx.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.api.entity.Pinlun;
import com.xxxx.api.mapper.PinlunMapper;
import com.xxxx.api.service.PinlunService;
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
@RequestMapping("/pinlun")
public class PinlunController {
    @Autowired(required = false)
    private PinlunMapper pinLunMapper;
    @Reference(interfaceClass = PinlunService.class)
    private PinlunService pinLunService;

    @PostMapping("/getPinlun")
    public Result getPinlun(@RequestBody Map<String,String> pinlun){
        Result result=new Result();
        QueryWrapper<Pinlun> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("bookname",pinlun.get("bookname"));
        List<Pinlun> list=pinLunMapper.selectList(queryWrapper);
        result.setMsg("成功");
        result.setCode("200");
        result.setData(list);
        return result;
    }
}
