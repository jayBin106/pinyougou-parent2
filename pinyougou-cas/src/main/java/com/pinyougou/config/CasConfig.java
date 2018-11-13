package com.pinyougou.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加过滤器，相当于web.xml
 * Created by lenovo on 2018/11/11.
 */
@Configuration
public class CasConfig {
    private static boolean casEnabled = true;
    @Value("${spring.cas.sign-out-filters}")
    private String signOutFilters;
    @Value("${spring.cas.auth-filters}")
    private String authFilters;
    @Value("${spring.cas.validate-filters}")
    private String validateFilters;
    @Value("${spring.cas.request-wrapper-filters}")
    private String wrapperFilters;
    @Value("${spring.cas.assertion-filters}")
    private String assertionFilters;
    @Value("${spring.cas.cas-server-login-url}")
    private String casServerLoginUrl;
    @Value("${spring.cas.cas-server-url-prefix}")
    private String casServerUrlPrefix;
    @Value("${spring.cas.redirect-after-validation}")
    private String redirectAfterValidation;
    @Value("${spring.cas.use-session}")
    private String useSession;
    @Value("${spring.cas.server-name}")
    private String serverName;

    //    	<!-- ======================== 单点登录开始 ======================== -->
//    <!-- 用于单点退出，该过滤器用于实现单点登出功能，可选配置 -->
    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<>();
        listener.setEnabled(casEnabled);
        listener.setListener(new SingleSignOutHttpSessionListener());
        listener.setOrder(1);
        return listener;
    }

    /**
     * 该过滤器用于实现单点登出功能，单点退出配置，一定要放在其他filter之前
     */
    @Bean
    public FilterRegistrationBean logOutFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        LogoutFilter logoutFilter = new LogoutFilter(casServerUrlPrefix + "/logout?service=" + serverName, new SecurityContextLogoutHandler());
        filterRegistration.setFilter(logoutFilter);
        filterRegistration.setEnabled(casEnabled);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add(signOutFilters);// 设置匹配的url
        if (urlPatterns.size() > 0)
            filterRegistration.setUrlPatterns(urlPatterns);
        else
            filterRegistration.addUrlPatterns("/*");
        filterRegistration.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
        filterRegistration.addInitParameter("serverName", serverName);
        filterRegistration.setOrder(2);
        return filterRegistration;
    }

    /**
     * 该过滤器用于实现单点登出功能，单点退出配置，一定要放在其他filter之前
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new SingleSignOutFilter());
        filterRegistration.setEnabled(casEnabled);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add(signOutFilters);// 设置匹配的url
        if (urlPatterns.size() > 0)
            filterRegistration.setUrlPatterns(urlPatterns);
        else
            filterRegistration.addUrlPatterns("/*");
        filterRegistration.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
        filterRegistration.addInitParameter("serverName", serverName);
        filterRegistration.setOrder(3);
        return filterRegistration;
    }

    /**
     * 该过滤器负责用户的认证工作
     */
    @Bean
    public FilterRegistrationBean authenticationFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new AuthenticationFilter());
        filterRegistration.setEnabled(casEnabled);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add(authFilters);// 设置匹配的url
        if (urlPatterns.size() > 0)
            filterRegistration.setUrlPatterns(urlPatterns);
        else
            filterRegistration.addUrlPatterns("/*");
        //casServerLoginUrl:cas服务的登陆url
        filterRegistration.addInitParameter("casServerLoginUrl", casServerLoginUrl);
        //本项目登录ip+port
        filterRegistration.addInitParameter("serverName", serverName);
        filterRegistration.addInitParameter("useSession", useSession);
        filterRegistration.addInitParameter("redirectAfterValidation", redirectAfterValidation);
        filterRegistration.setOrder(4);
        return filterRegistration;
    }

    /**
     * 该过滤器负责对Ticket的校验工作
     */
    @Bean
    public FilterRegistrationBean cas20ProxyReceivingTicketValidationFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        Cas20ProxyReceivingTicketValidationFilter cas20ProxyReceivingTicketValidationFilter = new Cas20ProxyReceivingTicketValidationFilter();
        cas20ProxyReceivingTicketValidationFilter.setServerName(serverName);
        filterRegistration.setFilter(cas20ProxyReceivingTicketValidationFilter);
        filterRegistration.setEnabled(casEnabled);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add(validateFilters);// 设置匹配的url
        if (urlPatterns.size() > 0)
            filterRegistration.setUrlPatterns(urlPatterns);
        else
            filterRegistration.addUrlPatterns("/*");
        filterRegistration.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
        filterRegistration.addInitParameter("serverName", serverName);
        filterRegistration.setOrder(5);
        return filterRegistration;
    }


    /**
     * 该过滤器对HttpServletRequest请求包装， 可通过HttpServletRequest的getRemoteUser()方法获得登录用户的登录名
     */
    @Bean
    public FilterRegistrationBean httpServletRequestWrapperFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new HttpServletRequestWrapperFilter());
        filterRegistration.setEnabled(true);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add(wrapperFilters);// 设置匹配的url
        if (urlPatterns.size() > 0)
            filterRegistration.setUrlPatterns(urlPatterns);
        else
            filterRegistration.addUrlPatterns("/login");
        filterRegistration.setOrder(6);
        return filterRegistration;
    }

    /**
     * 该过滤器使得可以通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。
     * 比如AssertionHolder.getAssertion().getPrincipal().getName()。
     * 这个类把Assertion信息放在ThreadLocal变量中，这样应用程序不在web层也能够获取到当前登录信息
     */
    @Bean
    public FilterRegistrationBean assertionThreadLocalFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new AssertionThreadLocalFilter());
        filterRegistration.setEnabled(true);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add(assertionFilters);// 设置匹配的url
        if (urlPatterns.size() > 0)
            filterRegistration.setUrlPatterns(urlPatterns);
        else
            filterRegistration.addUrlPatterns("/*");
        filterRegistration.setOrder(7);
        return filterRegistration;
    }

}
