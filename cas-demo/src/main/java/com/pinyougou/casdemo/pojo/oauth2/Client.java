package com.pinyougou.casdemo.pojo.oauth2;

/**
 * @author: wangsaichao
 * @date: 2018/5/11
 * @description: 客户端信息
 */
public class Client {

    private String id;
    private String clientName;
    private String clientId;
    private String clientSecret;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}