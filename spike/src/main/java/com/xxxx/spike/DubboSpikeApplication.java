package com.xxxx.spike;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xxxx.api")
@EnableDubboConfiguration
public class DubboSpikeApplication {
    public static void main(String[] args){
        SpringApplication.run(DubboSpikeApplication.class,args);
    }
}
