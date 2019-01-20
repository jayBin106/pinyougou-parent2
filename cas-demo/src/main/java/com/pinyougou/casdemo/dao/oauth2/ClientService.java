package com.pinyougou.casdemo.dao.oauth2;

import com.pinyougou.casdemo.pojo.oauth2.Client;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClientService {
    /**
     * 根据clientId查询Client信息
     */
    @Select(value = "select * from oauth2_client where client_id=#{client_id}")
    public Client findByClientId(@Param(value = "client_id") String clientId);

    /**
     * 根据clientSecret查询client信息
     */
    @Select(value = "select * from oauth2_client where client_secret=#{client_secret}")
    public Client findByClientSecret(@Param(value = "client_secret") String clientSecret);
}