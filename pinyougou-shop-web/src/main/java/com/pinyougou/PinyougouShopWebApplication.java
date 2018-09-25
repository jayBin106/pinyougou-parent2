package com.pinyougou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = {"classpath:pinyougou-shop-web.xml"})
public class PinyougouShopWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PinyougouShopWebApplication.class, args);
    }
}
