package com.pinyougou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.service.ItemCatService;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbItemCat> findAll() {
        return itemCatService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return itemCatService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param itemCat
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody String itemCatStr) {
        try {
            System.out.println(itemCatStr);
            JSONObject jsonObject = JSONObject.parseObject(itemCatStr);
            String id = jsonObject.getString("id");
            String id2 = jsonObject.getString("id2");
            TbItemCat itemCat = jsonObject.getObject("itemCat", TbItemCat.class);
            //一级菜单
            if (StringUtils.isEmpty(id) && StringUtils.isEmpty(id2)) {
                itemCat.setParentId(0L);
            } else if (StringUtils.isNotEmpty(id) && StringUtils.isEmpty(id2)) {
                itemCat.setParentId(Long.valueOf(id));
            } else if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(id2)) {
                itemCat.setParentId(Long.valueOf(id2));
            }
            itemCatService.add(itemCat);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param itemCat
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody  String itemCatStr) {
        try {
            System.out.println(itemCatStr);
            JSONObject jsonObject = JSONObject.parseObject(itemCatStr);
            String id = jsonObject.getString("id");
            String id2 = jsonObject.getString("id2");
            TbItemCat itemCat = jsonObject.getObject("itemCat", TbItemCat.class);
            //一级菜单
            if (StringUtils.isNotEmpty(id) && StringUtils.isEmpty(id2)) {
                itemCat.setParentId(Long.valueOf(id));
            } else if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(id2)) {
                itemCat.setParentId(Long.valueOf(id2));
            }
            itemCatService.updateByPrimaryKeySelective(itemCat);
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
    public TbItemCat findOne(Long id) {
        TbItemCat one = itemCatService.findOne(id);
        return one;
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findItemCatById")
    public String findItemCatById(Long id) {
        TbItemCat tbItemCat = itemCatService.findOne(id);
        Long parentId = tbItemCat.getParentId();
        Map<String, Object> map = new HashMap<>();
        Long id1 = 0L;
        String itemCatStr = "";
        itemCatStr = JSONObject.toJSONString(tbItemCat);
        if (parentId == 0L) {
            map.put("itemCat", itemCatStr);
        } else if (parentId > 0L) {
            TbItemCat tbItemCat1 = itemCatService.findOne(parentId);
            Long parentId2 = tbItemCat1.getParentId();
            if (parentId2 == 0L) {
                map.put("id", parentId);
                map.put("itemCat", itemCatStr);
            } else if (parentId2 > 0L) {
                map.put("id", parentId2);
                map.put("id2", parentId);
                map.put("itemCat", itemCatStr);
            }
        }
        String string = JSONObject.toJSONString(map);
        return string;
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
            itemCatService.delete(ids);
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
    public PageResult search(@RequestBody TbItemCat itemCat, int page, int rows) {
        return itemCatService.findPage(itemCat, page, rows);
    }

    /**
     * 根据父id查询数据
     *
     * @param itemCat
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/findByParentId")
    public PageResult findByParentId(Long parentId, int page, int rows) {
        return itemCatService.findByParentId(parentId, page, rows);
    }

}
