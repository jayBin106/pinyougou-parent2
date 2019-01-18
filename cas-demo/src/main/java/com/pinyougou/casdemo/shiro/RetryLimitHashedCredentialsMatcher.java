package com.pinyougou.casdemo.shiro;

import com.pinyougou.casdemo.dao.MemberDao;
import com.pinyougou.casdemo.pojo.Member;
import com.pinyougou.casdemo.until.ShiroRedisUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RetryLimitHashedCredentialsMatcher
 * <p>  登陆次数限制
 * liwenbin
 * 2019/1/17 9:59
 */
public class RetryLimitHashedCredentialsMatcher extends SimpleCredentialsMatcher {
    private static final Logger logger = Logger.getLogger(RetryLimitHashedCredentialsMatcher.class);
    @Autowired
    private MemberDao memberDao;

    @Autowired
    ShiroRedisUtils redisUtils;

    //redis登录次数前缀
    public final static String LOGINCOUNT = "loginCount:";

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String name = token.getPrincipal().toString();
        //获取用户登录次数
        Object countObj = redisUtils.get(LOGINCOUNT + name);
        Integer count = countObj != null ? Integer.valueOf(countObj.toString()) : 0;
        //如果用户没有登陆过,登陆次数加1 并放入缓存
        //如果用户错误次数大于5次，抛出锁定用户次数异常，并同步到数据库
        count++;
        if (count > 5) {
            //数据库字段 默认为 0  就是正常状态 所以 要改为1
            //修改数据库的状态字段为锁定
            Member member = memberDao.selectByPrimaryKey(name);
            if (member != null && member.getLocked() == 0) {
                memberDao.updateByPrimaryKey(1 + "", member.getMid());
            }
            logger.info("锁定用户" + member.getName());
            //抛出用户锁定异常
            throw new LockedAccountException();
        }
        redisUtils.set(LOGINCOUNT + name, count);
        //判断密码是否正确，如果正确清楚缓存
        boolean match = super.doCredentialsMatch(token, info);
        if (match) {
            //如果正确,从缓存中将用户登录计数 清除
            redisUtils.del(LOGINCOUNT + name);
        }
        return match;
    }

    /**
     * 结束锁定用户
     *
     * @param name
     */
    public void unLockAccount(String name) {
        Member member = memberDao.selectByPrimaryKey(name);
        if (member != null && member.getLocked() == 1) {
            memberDao.updateByPrimaryKey(0 + "", member.getMid());
            redisUtils.del(LOGINCOUNT + name);
            logger.info("解锁用户" + member.getName());
        }
    }
}
