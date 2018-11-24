package com.pinyougou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.service.GoodsService;
import com.pinyougou.solr.service.SolrSearchService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 根据商品的上下架，更新solr库
 * Created by lenovo on 2018/11/10.
 */
@Component
public class GoodsController {
    @Reference(version = "1.0.0")
    private SolrSearchService solrSearchService;
    @Reference(version = "2.0.0",timeout = 999999999)
    private GoodsService goodsService;

    @JmsListener(destination = "goodsId")
    public void updataSolr(Long goodsId){
        TbGoods tbGoods = goodsService.findOne(goodsId);
        if(tbGoods.getAuditStatus().equals("2")&& tbGoods.getIsMarketable().equals("1")){
            solrSearchService.add(goodsId);
        }else {
            solrSearchService.delete(goodsId);
        }
    }
}
