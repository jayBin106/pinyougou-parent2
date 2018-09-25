package com.pinyougou.serviceImpl;

import com.pinyougou.dao.TbAddressMapper;
import com.pinyougou.pojo.TbAddress;
import com.pinyougou.service.Itest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lenovo on 2018/9/25.
 */
@Service(value = "iSayHello")
public class TestImpl implements Itest {
    @Autowired
    TbAddressMapper tbAddressMapper;

    @Override
    public void say() {
        System.out.println("你好。。");
        TbAddress tbAddress = tbAddressMapper.selectByPrimaryKey(59l);
        System.out.println(tbAddress);
    }
}
