package com.pinyougou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@ImportResource(locations = {"classpath:static/spring-security.xml"})
//@EnableCasClient // 开启CAS支持
@EnableWebSecurity   //启动web安全
public class PinyougouCasApplication {
	public static void main(String[] args) {
		SpringApplication.run(PinyougouCasApplication.class, args);
	}
}
