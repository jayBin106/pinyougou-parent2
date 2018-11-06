package com.pinyougou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pinyougou.dao")
//@EnableConfigurationProperties(SolrProperties.class)
public class PinyougouContentServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PinyougouContentServiceApplication.class, args);
	}
}
