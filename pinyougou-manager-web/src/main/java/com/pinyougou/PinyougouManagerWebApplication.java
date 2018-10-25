package com.pinyougou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@ImportResource(locations = {"classpath:static/spring-security.xml"})
@MapperScan("com.pinyougou.dao")
@EnableWebSecurity   //启动web安全
//@ComponentScan(value = "com.pinyougou.*") // 默认扫描的路径
public class PinyougouManagerWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(PinyougouManagerWebApplication.class, args);
    }
}
