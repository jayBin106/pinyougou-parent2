package com.pinyougou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@ImportResource(locations = {"classpath:pinyougou-shop-web.xml", "classpath:static/spring-security.xml"})
@EnableWebSecurity   //启动web安全
public class PinyougouShopWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PinyougouShopWebApplication.class, args);
    }
}
