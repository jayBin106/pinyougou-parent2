package com.pinyougou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.pojo.TbContent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lenovo on 2018/10/25.
 */
@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference(version = "1.0.0")
    private ContentService contentService;

    /**
     * 根据广告分类ID查询广告列表
     *
     * @param categoryId
     * @return
     */
    @RequestMapping("/findByCategoryId")
    public List<TbContent> findByCategoryId(Long categoryId) {
        List<TbContent> byCategoryId = contentService.findByCategoryId(categoryId);
        return byCategoryId;
    }

}
