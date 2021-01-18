package com.macro.mall.mallwechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Date 2021/1/17 14:28
 * @Version 1.0
 * @Author Admin
 * @Copyright (c) 2020, 乐记
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MallWechatApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallWechatApplication.class, args);
    }
}
