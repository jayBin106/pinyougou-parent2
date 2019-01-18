package com.pinyougou.casdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableCasClient   //启动CAS @EnableCasClient
@SpringBootApplication
public class CasDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(CasDemoApplication.class, args);
    }
}

