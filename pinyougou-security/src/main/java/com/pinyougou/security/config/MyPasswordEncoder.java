package com.pinyougou.security.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * MyPasswordEncoder
 * <p>  密码加密类
 * liwenbin
 * 2019/1/24 10:50
 */
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePassWord = encoder.encode(charSequence);
        return encodePassWord;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(charSequence, s);
        return matches;
    }
}
