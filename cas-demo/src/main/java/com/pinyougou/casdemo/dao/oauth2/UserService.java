package com.pinyougou.casdemo.dao.oauth2;

import com.pinyougou.casdemo.pojo.oauth2.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserService {
    /**
     * 根据用户名 查询用户
     */
    public User findByUserName(String username);

    /**
     * 修改用户信息
     */
    public int updateUser(User user);
}