package com.dj.djbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.dj")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.dj.djbackendserviceclient.service"})
public class DjBackendJudgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DjBackendJudgeServiceApplication.class, args);
    }

}
