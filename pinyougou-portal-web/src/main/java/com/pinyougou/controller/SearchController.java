package com.pinyougou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.content.service.SolrSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by lenovo on 2018/11/10.
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    @Reference(version = "1.0.0", timeout = 999999)
    private SolrSearchService solrSearchService;
    /**
     * 综合查询: 在综合查询中, 有按条件查询, 条件过滤, 排序, 分页, 高亮显示, 获取部分域信息
     *
     * @return
     */
    @RequestMapping("/query")
    public Map<String, Object> search(@RequestBody Map searchMap) {
        Map<String, Object> search = solrSearchService.search(searchMap);
        return search;
    }
}
