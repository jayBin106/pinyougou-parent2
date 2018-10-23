package com.pinyougou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ImportResource(locations = {"classpath:pinyougou-shop-web.xml", "classpath:static/spring-security.xml"})
@EnableWebSecurity   //启动web安全
@EnableTransactionManagement  //启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
public class PinyougouShopWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(PinyougouShopWebApplication.class, args);
    }
}
