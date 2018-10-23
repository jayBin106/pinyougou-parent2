package com.pinyougou.controller;

import com.alibaba.fastjson.JSONObject;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.service.TypeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Autowired
    private TypeTemplateService typeTemplateService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbTypeTemplate> findAll() {
        return typeTemplateService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return typeTemplateService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param typeTemplate
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody String jsonStr) {
        try {
            System.out.println("新增数据");
            System.out.println(jsonStr);
            TbTypeTemplate typeTemplate = JSONObject.parseObject(jsonStr, TbTypeTemplate.class);
            typeTemplateService.add(typeTemplate);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody String jsonStr) {
        try {
            System.out.println("更新数据");
            System.out.println(jsonStr);
            TbTypeTemplate typeTemplate = JSONObject.parseObject(jsonStr, TbTypeTemplate.class);
            typeTemplateService.update(typeTemplate);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbTypeTemplate findOne(Long id) {
        return typeTemplateService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            typeTemplateService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param brand
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbTypeTemplate typeTemplate, int page, int rows) {
        return typeTemplateService.findPage(typeTemplate, page, rows);
    }

    /**
     * 下拉
     *
     * @return
     */
    @RequestMapping("/selectOptionList")
    public String selectOptionList() {
        List<TbTypeTemplate> all = typeTemplateService.findAll();
        List<Map<String, String>> maps = new ArrayList<>();
        for (TbTypeTemplate o : all) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", o.getId().toString());
            map.put("text", o.getName());
            maps.add(map);
        }
        String string = JSONObject.toJSONString(maps);
        return string;
    }

}
