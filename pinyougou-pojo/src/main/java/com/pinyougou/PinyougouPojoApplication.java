package com.pinyougou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pinyougou.dao")
public class PinyougouPojoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PinyougouPojoApplication.class, args);
	}
}
