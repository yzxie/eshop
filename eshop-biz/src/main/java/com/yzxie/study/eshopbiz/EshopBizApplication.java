package com.yzxie.study.eshopbiz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan("com.yzxie.study.eshopbiz.repository")
public class EshopBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopBizApplication.class, args);
    }

}
