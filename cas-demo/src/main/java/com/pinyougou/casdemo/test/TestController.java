package com.pinyougou.casdemo.test;

import com.pinyougou.casdemo.shiro.MyByteSource;
import com.pinyougou.casdemo.until.ShiroRedisUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * TestController
 * <p> md5加盐加密
 * liwenbin
 * 2019/1/21 10:16
 */
@Controller
public class TestController {
    @Autowired
    ShiroRedisUtils shiroRedisUtils;

    @Test
    public void test() throws Exception {
        System.out.println(md5("123456", new MyByteSource("admin")));
        Object o = shiroRedisUtils.get("shiro:session:81271ff6-87af-48c2-a2da-70cacfbe96af");
        System.out.println(o.toString());
    }

    public static final String md5(String password, MyByteSource salt) {
        //加密方式
        String hashAlgorithmName = "MD5";
        //盐：为了即使相同的密码不同的盐加密后的结果也不同
        ByteSource byteSalt = ByteSource.Util.bytes(salt);
        //密码
        Object source = password;
        //加密次数
        int hashIterations = 2;
        SimpleHash result = new SimpleHash(hashAlgorithmName, source, byteSalt, hashIterations);
        return result.toString();
    }
}
