package com.yzxie.study.eshopqueue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yzxie.study.eshopqueue.repository")
public class EshopQueueApplication {

	public static void main(String[] args) {
		SpringApplication.run(EshopQueueApplication.class, args);
	}

}
