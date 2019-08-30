package com.yzxie.study.seckillbiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SeckillBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillBizApplication.class, args);
    }

}
