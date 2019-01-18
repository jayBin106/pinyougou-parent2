package com.pinyougou.casdemo.shiro;

import com.pinyougou.casdemo.until.ShiroRedisUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ShiroSessionListener
 * <p>
 * liwenbin 配置session监听器
 * 2019/1/16 13:48
 */
public class ShiroSessionListener implements SessionListener {
    /**
     * 统计在线人数
     * juc包下线程安全自增
     */
    public final static String ONLINE_COUNT = "ONLINE_COUNT";
    private ShiroRedisUtils redisUtils;

    public ShiroSessionListener(ShiroRedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    /**
     * 会话创建时触发
     *
     * @param session
     */
    @Override
    public void onStart(Session session) {
        redisUtils.incr(ONLINE_COUNT, 1l);
    }

    /**
     * 退出会话时触发
     *
     * @param session
     */
    @Override
    public void onStop(Session session) {
        redisUtils.decr(ONLINE_COUNT, 1l);
    }

    /**
     * 会话过期时触发
     *
     * @param session
     */
    @Override
    public void onExpiration(Session session) {
        redisUtils.decr(ONLINE_COUNT, 1l);
    }

    /**
     * 获取在线人数使用
     *
     * @return
     */
    public Integer getSessionCount() {
        return redisUtils.get(ONLINE_COUNT) == null ? 0 : Integer.valueOf(redisUtils.get(ONLINE_COUNT).toString());
    }
}
