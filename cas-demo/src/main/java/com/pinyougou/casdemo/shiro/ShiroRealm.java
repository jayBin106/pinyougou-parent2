package com.pinyougou.casdemo.shiro;

import com.alibaba.fastjson.JSONArray;
import com.pinyougou.casdemo.dao.ActionDao;
import com.pinyougou.casdemo.dao.MemberDao;
import com.pinyougou.casdemo.dao.RoleDao;
import com.pinyougou.casdemo.pojo.Action;
import com.pinyougou.casdemo.pojo.Member;
import com.pinyougou.casdemo.pojo.Role;
import com.pinyougou.casdemo.until.EncryptionUtil;
import com.pinyougou.casdemo.until.ListToSetUtil;
import com.pinyougou.casdemo.until.ShiroRedisUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

/**
 * ShiroRealm
 * <p>
 * 在Shiro中，最终是通过Realm来获取应用程序中的用户、角色及权限信息的
 * * 在Realm中会直接从我们的数据源中获取Shiro需要的验证信息。可以说，Realm是专用于安全框架的DAO.
 * <p>
 * liwenbin
 * 2019/1/13 15:44
 */
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ActionDao actionDao;

    //redis角色前缀
    public final static String PERMISSIONS = "PERMISSIONS:";
    //redis权限前缀
    public final static String AUTHROLE = "AUTHROLE:";

    private ShiroRedisUtils shiroRedisUtils;

    public void setShiroRedisUtils(ShiroRedisUtils shiroRedisUtils) {
        this.shiroRedisUtils = shiroRedisUtils;
    }

    /**
     * 用户认证
     *
     * @param atoken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken atoken) throws AuthenticationException {
        //获取密码第一种方式
//        String username = atoken.getPrincipal().toString();
//        String password = new String((char[]) atoken.getCredentials());
        //第二种方式
        UsernamePasswordToken token = (UsernamePasswordToken) atoken;
        String name = token.getUsername();
        String pass = new String(token.getPassword());
        String md5Encode = EncryptionUtil.md5Encode(pass);
        //从数据库查询用户信息
        Member member = memberDao.selectByPrimaryKey(name);
        if (member == null) {
            throw new UnknownAccountException("用户名或密码错误");
        }
        if (!md5Encode.equals(member.getPassword())) {
            throw new IncorrectCredentialsException("用户名或密码错误！");
        }
        if ("1".equals(member.getLocked())) {
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        }
        //这里验证authenticationToken和simpleAuthenticationInfo的信息
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(member, pass, getName());
        return info;
    }

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取用户
        Member member = (Member) SecurityUtils.getSubject().getPrincipal();
        //获取用户
        //Member m = (Member) principalCollection.getPrimaryPrincipal();
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //角色
        String roleKey = AUTHROLE + member.getMid();
        Object roleobj = shiroRedisUtils.get(roleKey);
        if (roleobj != null) {
            List<String> roleList = JSONArray.parseArray(roleobj.toString(), String.class);
            Set<String> roleSet = ListToSetUtil.listToSet(roleList);
            simpleAuthorizationInfo.setRoles(roleSet);
        } else {
            //获取用户角色
            Set<Role> roles = roleDao.getRole(member.getMid());
            //添加角色
            for (Role role : roles) {
                simpleAuthorizationInfo.addRole(role.getFlag());
            }
            //角色
            Set<String> roles1 = simpleAuthorizationInfo.getRoles();
            shiroRedisUtils.set(roleKey, roles1);
        }
        //添加权限
        String perMissKey = PERMISSIONS + member.getMid();
        Object perMiss = shiroRedisUtils.get(perMissKey);
        if (perMiss != null) {
            List<String> perMissList = JSONArray.parseArray(perMiss.toString(), String.class);
            Set<String> perMissSet = ListToSetUtil.listToSet(perMissList);
            simpleAuthorizationInfo.setStringPermissions(perMissSet);
        } else {
            //获取用户权限
            Set<Action> actions = actionDao.getAction(member.getMid());
            for (Action action : actions) {
                simpleAuthorizationInfo.addStringPermission(action.getFlag());
            }
            //权限
            Set<String> stringPermissions = simpleAuthorizationInfo.getStringPermissions();
            shiroRedisUtils.set(perMissKey, stringPermissions);
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 修改授权后清空缓存中的授权信息(修改权限的service中调用)
     */
    public void clearCached() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }

    /**
     * 重写方法，清楚当前用户的授权缓存
     *
     * @param principals
     */
    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 重写方法，清除当前用户的 认证缓存
     *
     * @param principals
     */
    @Override
    protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    protected void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法：清除所有 授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}
