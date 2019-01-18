package com.pinyougou.casdemo.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.pinyougou.casdemo.until.ShiroRedisUtils;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * ShiroConfiguration
 * <p> Shiro配置文件
 * liwenbin
 * 2019/1/13 15:47
 */
@Configuration
public class ShiroConfiguration {
    @Value("${spring.redis.host}")
    String redisHost;
    @Value("${spring.redis.port}")
    String redisPort;

    @Autowired
    ShiroRedisUtils shiroRedisUtils;

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * * 注意：初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * * Web应用中,Shiro可控制的Web请求必须经过Shiro主过滤器的拦截
     * Filter工厂，设置对应的过滤条件和跳转条件
     * <p>
     * <p>
     * anon:所有url都都可以匿名访问;
     * authc: 需要认证才能进行访问;
     * user:配置记住我或认证通过可以访问；
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(org.apache.shiro.mgt.SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //必须设置 SecurityManager,Shiro的核心安全接口
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //登录
        //这里的/login是后台的接口名,非页面，如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
//        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setLoginUrl("/");
        //首页
        //这里的/index是后台的接口名,非页面,登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //错误页面，认证不通过跳转
        //未授权界面,该配置无效，并不会进行页面跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        //自定义拦截器限制并发人数,参考博客：
        LinkedHashMap<String, Filter> filtersMap = new LinkedHashMap<>();
        //限制同一帐号同时在线的个数
        filtersMap.put("kickout", kickoutSessionControlFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
//        shiroFilterFactoryBean.setFilters(filtersMap);
        // 配置访问权限 必须是LinkedHashMap，因为它必须保证有序
        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 一定要注意顺序,否则就不好使了
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        //配置不登录可以访问的资源，anon 表示资源都可以匿名访问
        linkedHashMap.put("/login", "kickout,anon");
        linkedHashMap.put("/unlockAccount", "anon"); //解锁用户权限
        linkedHashMap.put("/", "kickout,anon");
        linkedHashMap.put("/css/**", "kickout,anon");
        linkedHashMap.put("/js/**", "kickout,anon");
        linkedHashMap.put("/img/**", "kickout,anon");
        linkedHashMap.put("/druid/**", "kickout,anon");
        linkedHashMap.put("/addAction", "kickout,anon");
        linkedHashMap.put("/doLogin", "kickout,anon");  //对所有用户认证
        linkedHashMap.put("/Captcha.jpg", "anon");      //图片验证码，对所有用户认证
        //logout是shiro提供的过滤器
        linkedHashMap.put("/logout", "anon");
        //此时访问/userInfo/del需要del权限,在自定义Realm中为用户授权。
        //filterChainDefinitionMap.put("/userInfo/del", "perms[\"userInfo:del\"]");
        //其他资源都需要认证  authc 表示需要认证才能进行访问,user表示认证或者记住我可以访问
        //解释： filterChainDefinitionMap.put("/**", "kickout,user"); 表示 访问/**下的资源
        //*******首先要通过 kickout 后面的filter，然后再通过user后面对应的filter才可以访问。
        linkedHashMap.put("/**", "kickout,user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(linkedHashMap);
        return shiroFilterFactoryBean;
    }
//##################################################################################################################

    /**
     * 配置核心安全事务管理器
     * 权限管理，配置主要是Realm的管理认证
     *
     * @return
     */
    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置自定义realm
        securityManager.setRealm(ShiroRealm());
        //配置记住我
        securityManager.setRememberMeManager(rememberMeManager());
        //配置自定义session管理
        securityManager.setSessionManager(sessionManager());
        //配置缓存管理器，单节点可以用ehcache缓存，分布式用redis缓存
        securityManager.setCacheManager(redisCacheManager());
        return securityManager;
    }
//##################################################################################################################

    /**
     * FormAuthenticationFilter 过滤器 过滤记住我
     * 记住我Filter
     *
     * @return
     */
    @Bean
    public FormAuthenticationFilter formAuthenticationFilter() {
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        //对应前端的checkbox的name = rememberMe
        formAuthenticationFilter.setRememberMeParam("rememberMe");
        return formAuthenticationFilter;
    }

    /**
     * 记住我管理器
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        manager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return manager;
    }


    /**
     * 记住我cookie
     * cookie对象;会话Cookie模板 ,默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid或rememberMe，自定义
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：
        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        cookie.setMaxAge(2592000);
        return cookie;
    }

    //##################################################################################################################
    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    //##################################################################################################################
    //缓存管理器
    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return ehCacheManager;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //redis中针对不同用户缓存
        redisCacheManager.setPrincipalIdFieldName("mid");
        //用户权限信息缓存时间
        redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }

    //##################################################################################################################
    //将自己的验证方式加入容器
    @Bean
    public ShiroRealm ShiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCachingEnabled(true);
        //启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
        shiroRealm.setAuthenticationCachingEnabled(true);
        //缓存AuthenticationInfo信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
        shiroRealm.setAuthenticationCacheName("authenticationCache");
        //启用授权缓存，即缓存AuthorizationInfo信息，默认false
        shiroRealm.setAuthorizationCachingEnabled(true);
        //缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
        shiroRealm.setAuthorizationCacheName("authorizationCache");
        //配置自定义密码比较器
        shiroRealm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher());
        return shiroRealm;
    }

    //##################################################################################################################

    /**
     * 必须（thymeleaf页面使用shiro标签控制按钮是否显示）
     * 未引入thymeleaf包，Caused by: java.lang.ClassNotFoundException: org.thymeleaf.dialect.AbstractProcessorDialect
     *
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    //##################################################################################################################

    /**
     * *******
     * 让某个实例的某个方法的返回值注入为Bean的实例
     * Spring静态注入
     *
     * @return
     */
    public MethodInvokingFactoryBean invokingFactoryBean() {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        bean.setArguments(new Object[]{securityManager()});
        return bean;
    }

    //##################################################################################################################

    /**
     * 配置session监听
     *
     * @return
     */
    @Bean
    public ShiroSessionListener sessionListener() {
        //测试在线人数
        ShiroSessionListener listener = new ShiroSessionListener(shiroRedisUtils);
        return listener;
    }

    /**
     * 配置会话ID生成器
     *
     * @return
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     *
     * @return
     */
    @Bean
    public SessionDAO sessionDAO() {
//        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
//        sessionDAO.setCacheManager(ehCacheManager());
//        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
//        sessionDAO.setSessionIdGenerator(sessionIdGenerator());

        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        sessionDAO.setRedisManager(redisManager());
        //session在redis中的保存时间,最好大于session会话超时时间
        sessionDAO.setExpire(12000);
        return sessionDAO;
    }

    /**
     * 配置保存sessionId的cookie
     * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
     *
     * @return
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("sessionId");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    //自定义会话管理配置
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager manager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        ((ArrayList<SessionListener>) listeners).add(sessionListener());
        manager.setSessionListeners(listeners);
        manager.setSessionIdCookie(sessionIdCookie());
        manager.setSessionDAO(sessionDAO());
        manager.setCacheManager(redisCacheManager());
        //会话超时//全局会话超时时间（单位毫秒），默认30分钟
        manager.setGlobalSessionTimeout(3600000);
        //是否开启删除无效的session对象  默认为true
        manager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        manager.setSessionValidationSchedulerEnabled(true);
        //定时清理失效会话, 清理用户直接关闭浏览器造成的孤立会话
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        manager.setSessionValidationInterval(3600000);
        //取消url 后面的 JSESSIONID
        manager.setSessionIdUrlRewritingEnabled(false);
        return manager;
    }

//##################################################################################################################

    /**
     * 并发登录控制
     *
     * @return
     */
    public KickoutSessionControlFilter kickoutSessionControlFilter() {
        /**
         * 5个参数的意思
         * 被踢出后重定向到的地址；
         *  是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；
         *  同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录
         *  用于根据会话ID，获取会话进行踢出操作的；
         *  使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
         */
        KickoutSessionControlFilter filter = new KickoutSessionControlFilter("/login?kickout=1", false, 1, sessionManager(), shiroRedisUtils);
        //用于根据会话ID，获取会话进行踢出操作的；
        return filter;
    }
    //##################################################################################################################

    /**
     * 配置密码比较器
     *
     * @return
     */
    @Bean
    public RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher() {
        RetryLimitHashedCredentialsMatcher matcher = new RetryLimitHashedCredentialsMatcher();
        //如果密码加密可以打开下面的配置
        return matcher;
    }
    //##################################################################################################################

    /**
     * redis缓存管理器
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager manager = new RedisManager();
        manager.setHost(redisHost);
        manager.setPort(Integer.valueOf(redisPort));
        return manager;
    }

//    @Bean
//    public ShiroRedisUtils shiroRedisUtils() {
//        ShiroRedisUtils redisUtils = new ShiroRedisUtils();
//        return redisUtils;
//    }
}
