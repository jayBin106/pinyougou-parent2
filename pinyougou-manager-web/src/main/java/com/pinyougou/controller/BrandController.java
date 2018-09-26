package com.pinyougou.controller;

import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.service.BrandService;
import com.pinyougou.service.Itest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lenovo on 2018/9/26.
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    BrandService brandService;

    @Autowired
    Itest itest;

    @RequestMapping("/findAll")
    public String t1() {
        System.out.println("get方法。。");
        String say = itest.say();
        return say;
    }

    @RequestMapping("/findPage")
    public PageResult findPage(int page, int size) {
        PageResult page1 = brandService.findPage(page, size);
        return page1;
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody TbBrand tbBrand, int page, int size) {
        PageResult page1 = brandService.findPage(tbBrand, page, size);
        return page1;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand tbBrand) {
        Result add = brandService.add(tbBrand);
        return add;
    }

    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand tbBrand) {
        Result add = brandService.update(tbBrand);
        return add;
    }

    @RequestMapping("/delete")
    public Result delete(long[] ids) {
        Result add = brandService.delete(ids);
        return add;
    }

    @RequestMapping("/selectOne")
    public TbBrand delete(long id) {
        TbBrand add = brandService.selectOne(id);
        return add;
    }


}
