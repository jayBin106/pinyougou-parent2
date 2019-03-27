package com.pinyougou.casdemo2;

import com.pinyougou.casdemo2.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CasDemo2ApplicationTests {
    @Autowired
    RedisUtil redisUtils;


    @Test
    public void contextLoads() {
    }

    @Test
    public void get() {
        Object a = redisUtils.get("a");
        System.out.println(a);
    }

}

