package com.pinyougou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2018/9/26.
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference(version = "1.0.0")
    BrandService brandService;

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

    @RequestMapping("/selectOptionList")
    public String selectOptionList() {
        PageResult add = brandService.findPage(1, 100);
        List<TbBrand> row = add.getRow();
        List<Map<String, String>> maps = new ArrayList<>();
        for (TbBrand o : row) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", o.getId().toString());
            map.put("text", o.getName());
            maps.add(map);
        }
        String string = JSONObject.toJSONString(maps);
        return string;
    }
}
