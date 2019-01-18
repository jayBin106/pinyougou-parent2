package com.pinyougou.casdemo.shiro;

import com.alibaba.fastjson.JSONArray;
import com.pinyougou.casdemo.pojo.Member;
import com.pinyougou.casdemo.until.ShiroRedisUtils;
import com.pinyougou.casdemo.until.SerializeUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * KickoutSessionControlFilter
 * <p>  shiro 自定义filter 实现 并发登录控制
 * liwenbin
 * 2019/1/16 15:06
 * <p>
 * 我们首先看一下 isAccessAllowed() 方法，在这个方法中，如果返回 true，
 * 则表示“通过”，走到下一个过滤器。如果没有下一个过滤器的话，表示具有了
 * 访问某个资源的权限。如果返回 false，则会调用 onAccessDenied 方法，去实
 * 现相应的当过滤不通过的时候执行的操作，例如检查用户是否已经登陆过,
 * 如果登陆过,根据自定义规则选择踢出前一个用户 还是 后一个用户。
 * onAccessDenied方法 返回 true 表示 自己处理完成,然后继续拦截器链执行。
 * 只有当两者都返回false时,才会终止后面的filter执行。
 */
public class KickoutSessionControlFilter extends AccessControlFilter {
    private ShiroRedisUtils redisUtils;
    /**
     * 踢出后到的地址
     */
    private String kickoutUrl;
    /**
     * 踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
     */
    private boolean kickoutAfter = false;

    /**
     * 同一个帐号最大会话数 默认1
     */
    private int maxSession = 1;
    private SessionManager sessionManager;


    public static final String KICKOUT_KEY = "shiro:cache:kickout:";

    public KickoutSessionControlFilter() {
    }

    public KickoutSessionControlFilter(String kickoutUrl, boolean kickoutAfter, int maxSession, SessionManager sessionManager, ShiroRedisUtils shiroRedisUtils) {
        this.kickoutUrl = kickoutUrl;
        this.kickoutAfter = kickoutAfter;
        this.maxSession = maxSession;
        this.sessionManager = sessionManager;
        this.redisUtils = shiroRedisUtils;
    }

    /**
     * 是否允许访问，返回true表示允许
     *
     * @param servletRequest
     * @param servletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    /**
     * 表示访问拒绝时是否自己处理，
     * 如果
     * 返回true表示自己不处理且继续拦截器链执行，
     * 返回false表示自己已经处理了（比如重定向到另一个页面）。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }
        Session session = subject.getSession();
        //这里获取的User是实体 因为我在 自定义ShiroRealm中的doGetAuthenticationInfo方法中
        //new SimpleAuthenticationInfo(user, password, getName()); 传的是 User实体
        // 所以这里拿到的也是实体,如果传的是userName 这里拿到的就是userName
        Member member = (Member) subject.getPrincipal();
        String name = member.getMid();

        // 初始化用户的队列放到缓存里
        String key = KICKOUT_KEY + name;
        Object keyobj = redisUtils.get(key);
        Deque<Serializable> deque = new LinkedList<Serializable>();
        if (keyobj != null) {
            List<Serializable> serializables = JSONArray.parseArray(keyobj.toString(), Serializable.class);
            for (Serializable serializable : serializables) {
                ((LinkedList<Serializable>) deque).add(serializable);
            }
        }
        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        Serializable sessionId = session.getId();
        if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            deque.push(sessionId);
        }

        //如果队列里的sessionId数超出最大会话数，开始踢人
        while (deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if (kickoutAfter) {
                ////如果踢出后者
                kickoutSessionId = deque.getFirst();
                kickoutSessionId = deque.removeFirst();
            } else {
                //踢出前者
                kickoutSessionId = deque.removeLast();
            }
            redisUtils.set(key, deque);//将sessionid存入redis

            //要踢出的session
            try {
                Session sessionKill = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if (sessionKill != null) {
                    sessionKill.setAttribute("kickout", true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null) {
            //会话被踢出了
            try {
                subject.logout();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //重定向到踢出前的页面
            WebUtils.issueRedirect(servletRequest, servletResponse, kickoutUrl);
            return false;
        }
        return true;
    }

}
