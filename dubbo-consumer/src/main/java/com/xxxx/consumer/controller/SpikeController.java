package com.xxxx.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.xxxx.api.entity.Book;
import com.xxxx.api.entity.Spikebook;
import com.xxxx.api.entity.dto.spikebookDto;
import com.xxxx.api.mapper.BookMapper;
import com.xxxx.api.mapper.SpikebookMapper;
import com.xxxx.api.service.SpikeService;
import com.xxxx.api.service.SpikebookService;
import com.xxxx.api.uilt.JwtTokenUtils;
import com.xxxx.api.uilt.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-30
 */
@RestController
@RequestMapping("/spike")
public class SpikeController {
    private static final String FAIL_BUYED = "已经买过了";
    private static final String BUYE_SUCCESS = "抢到了，订单生成中";
    private static final String FAIL_SOLD_OUT = "没货了";
    private static final String FAIL_BUSY = "排队中，请重试！";
    @Reference(interfaceClass = SpikebookService.class)
    private SpikebookService spikebookService;

    @Autowired(required = false)
    private BookMapper bookMapper;
    @Autowired(required = false)
    private SpikebookMapper spikebookMapper;
    @RequestMapping("/rest")
    public Result test(){
        Result result=new Result();
        result.setCode("200");
        result.setMsg("初始化秒杀商品成功");
        spikebookService.setSpikeBook();
        return result;
    }
    @RequestMapping("/getSpike")
    public Result test(@RequestBody HashMap<String, String> hashMap){
        System.out.println(hashMap.get("time"));
        Result result=new Result();
        result.setCode("200");
        result.setMsg("获取秒杀商品成功");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(hashMap.get("time"),df);
        List<spikebookDto> list=spikebookService.getSpike_Book_Main(ldt);
        result.setData(list);
        return result;
    }
    @RequestMapping("/addOrder")
    public Result addOrder(@RequestBody HashMap<String,String> hashMap) {
        Result result=new Result();
        String ky=spikebookService.insertOrder(JwtTokenUtils.getUsername(hashMap.get("token")),hashMap.get("bookname"));
        switch (ky){
            case FAIL_BUYED:
                result.setCode("400");
                result.setData(FAIL_BUYED);
                result.setMsg("已经买过");
                break;
            case BUYE_SUCCESS:
                result.setCode("200");
                result.setData(BUYE_SUCCESS);
                result.setMsg("抢到了，订单生成中");
                break;
            case FAIL_SOLD_OUT:
                result.setCode("400");
                result.setData(FAIL_BUYED);
                result.setMsg("没货了");
                break;
            case FAIL_BUSY:
                result.setCode("400");
                result.setData(FAIL_BUYED);
                result.setMsg("排队中，请重试！");
                break;
        }
        return result;
    }

    @GetMapping("/addSpike")
    public Result addspike() {
        spikebookMapper.deleteUser();
        Result result=new Result();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int k=day;
        int bg=k;
        LocalDateTime localDateTimeOf = LocalDateTime.of(2021, month, k, 0, 0,0);
        List<Book> list=bookMapper.selectList(null);
        Long j=0L;
        for (int i = 0; i<list.size();i++){
            Spikebook spikebook=new Spikebook();
            spikebook.setBookname(list.get(i).getName());
            spikebook.setSpike(false);
            spikebook.setSize(100);
            spikebook.setStart(localDateTimeOf.plusHours(j));
            spikebook.setEnd(localDateTimeOf.plusHours(j+2L));
            spikebook.setSize(100);
            spikebook.setNowsize(100);
            spikebookService.save(spikebook);
            if (i%8==0&&i!=0){
                j=j+2L;
            }
            if (i%(8*12-1)==0&&i!=0){
                localDateTimeOf = localDateTimeOf.plusDays(1);
                j=0L;
            }
            if (k-bg>4){
                break;
            }
        }
        result.setCode("200");
        result.setMsg("设置秒杀商品成功");
        result.setData(spikebookMapper.selectList(null));
        return result;
    }
}
