package com.pinyougou.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.pinyougou.dao.TbAddressMapper;
import com.pinyougou.dao.TbBrandMapper;
import com.pinyougou.pojo.TbAddress;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.service.Itest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lenovo on 2018/9/25.
 */
@Service(value = "iSayHello")
public class TestImpl implements Itest {
    @Autowired
    TbBrandMapper tbBrandMapper;

    @Override
    public String say() {
        System.out.println("你好。。");
        TbBrandExample example = new TbBrandExample();
        List<TbBrand> tbAddress = tbBrandMapper.selectByExample(example);
        String string = JSONObject.toJSONString(tbAddress);
        System.out.println(string);
        return string;
    }
}
