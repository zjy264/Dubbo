package com.xxxx.provider;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xxxx.api")
@EnableDubboConfiguration
public class DubboProviderApplication {
    public static void main(String[] args){
        SpringApplication.run(DubboProviderApplication.class,args);
    }
}
