package com.pinyougou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.content.service.ItemPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 商品详情页
 * Created by lenovo on 2018/11/1.
 */
@Controller
@RequestMapping("/goodsDetail")
public class GoodsDetailsController {
    @Reference(version = "1.0.0", timeout = 40000)
    private ItemPageService itemPageService;

    /**
     * 商品详情页
     *
     * @param goodId
     * @param modelMap
     * @return
     */
    @GetMapping("/genHtml")
    public String genHtml(Long goodId, ModelMap modelMap) {
        ModelMap modelMap1 = itemPageService.genItemHtml(goodId);
        for (String s : modelMap1.keySet()) {
            Object o = modelMap1.get(s);
            modelMap.put(s, o);
        }
        return "item";
    }
}
