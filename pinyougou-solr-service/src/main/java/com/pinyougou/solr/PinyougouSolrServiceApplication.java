package com.pinyougou.solr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pinyougou.dao")
public class PinyougouSolrServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PinyougouSolrServiceApplication.class, args);
    }
}