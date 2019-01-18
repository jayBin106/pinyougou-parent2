package com.pinyougou.casdemo;

import com.pinyougou.casdemo.until.ShiroRedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CasDemoApplicationTests {
    @Autowired
    ShiroRedisUtils redisUtils;

    @Test
    public void contextLoads() {
    }

    @Test
    public void get() {
        Object a = redisUtils.get("a");
        System.out.println(a);
    }
}

