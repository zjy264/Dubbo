package com.xxxx.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.api.entity.Shopping;
import com.xxxx.api.mapper.ShoppingMapper;
import com.xxxx.api.service.ShoppingService;
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
@RequestMapping("/shopping")
public class ShoppingController {
    @Reference(interfaceClass = ShoppingService.class)
    private ShoppingService shoppingService;

    @Autowired(required = false)
    private ShoppingMapper shoppingMapper;

    @PostMapping("/getOrder")
    public Result getOrder(@RequestBody Map<String,String> shop){
        Result result=new Result();
        result.setCode("200");
        result.setData(shoppingService.getBookShopping(shop.get("username")));
        return result;
    }
    @PostMapping("/addOrder")
    public Result addOrder(@RequestBody Shopping shopping){
        Result result=new Result();
        shoppingService.save(shopping);
        result.setCode("200");
        result.setData(shopping);
        return result;
    }
    ///下面是购物车
    @PostMapping("/getShop")
    public Result getShop(@RequestBody Map<String,String> shop){
        Result result=new Result();
        result.setCode("200");
        result.setData(shoppingService.getBookShopping_no_buy(shop.get("username")));
        return result;
    }
    @PostMapping("/getShopSize")
    public Result getShopSize(@RequestBody Map<String,String> shop){
        try{
            QueryWrapper<Shopping> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("username",shop.get("username"));
            queryWrapper1.eq("buy",0);
            List<Shopping> shoppingj=shoppingMapper.selectList(queryWrapper1);
            Result result=new Result();
            result.setCode("200");
            result.setData(shoppingj.size());
            return result;
        }catch (Exception e){
            Result result=new Result();
            result.setCode("400");
            result.setMsg("参数不全");
            result.setData(null);
            return result;
        }

    }
    @PostMapping("/addShop")
    public Result addShop(@RequestBody Map<String,String> shop){
        try{
            QueryWrapper<Shopping> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("book_name",shop.get("bookname"));
            queryWrapper.eq("username",shop.get("username"));
            queryWrapper.eq("buy",0);
            if (shop.get("addorremove").equals("0")) {
                shoppingService.removeById(shoppingMapper.selectList(queryWrapper).get(0).getId());
                Result result=new Result();
                result.setCode("200");
                result.setMsg("删除购物车物品成功");
                result.setData(null);
                return result;
            }else {
                List<Shopping> shoppings=shoppingMapper.selectList(queryWrapper);
                if (shoppings.size()!=0){
                    Shopping shopping2=new Shopping();
                    int num = Integer.parseInt(shoppings.get(0).getSize())+ 1;
                    shopping2.setSize(String.valueOf(num));
                    shoppingService.update(shopping2,queryWrapper);
                }else {
                    Shopping shopping=new Shopping();
                    shopping.setUsername(shop.get("username"));
                    shopping.setBookName(shop.get("bookname"));
                    shoppingService.save(shopping);
                }
                Result result=new Result();
                result.setCode("200");
                result.setData(null);
                return result;
            }

        }catch (Exception e){
            Result result=new Result();
            result.setCode("400");
            result.setData(null);
            return result;
        }

    }
    @PostMapping("/removeShop")
    public Result RemoveShop(@RequestBody Map<String,String> shop){
        Result result=new Result();
        QueryWrapper<Shopping> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",shop.get("username"));
        queryWrapper.eq("book_name",shop.get("book_name"));
        System.out.println(shop);
        boolean remove=shoppingService.remove(queryWrapper);
        result.setData(remove);
        result.setCode("200");
        result.setMsg("执行删除");
        return result;
    }
}
