package com.pinyougou.security.config;

import com.pinyougou.security.dao.MemberDao;
import com.pinyougou.security.dao.RoleDao;
import com.pinyougou.security.pojo.Member;
import com.pinyougou.security.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * MyUserService
 * <p>   管理登录用户
 * liwenbin
 * 2019/1/24 10:46
 */
@Component
public class MyUserService implements UserDetailsService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberDao.selectByPrimaryKey(username);
        if (member != null) {
            Set<Role> role = roleDao.getRole(username);
            List<GrantedAuthority> list = new ArrayList<>();
            for (Role role1 : role) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role1.getFlag());
                //1：此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
                list.add(grantedAuthority);
            }
            return new User(member.getMid(), member.getPassword(), list);
        } else {
            throw new UsernameNotFoundException("用户: " + username + " do not exist!");
        }
    }
}
