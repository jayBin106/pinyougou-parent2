package com.pinyougou.solr.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.dao.TbGoodsDescMapper;
import com.pinyougou.dao.TbGoodsMapper;
import com.pinyougou.dao.TbItemCatMapper;
import com.pinyougou.dao.TbItemMapper;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.solr.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import java.util.List;

/**
 * Created by lenovo on 2018/11/2.
 */
@Service(version = "1.0.0",group = "solr")
public class ItemPageServiceImpl implements ItemPageService {
    @Autowired
    TbGoodsDescMapper tbGoodsDescMapper;
    @Autowired
    TbGoodsMapper tbGoodsMapper;
    @Autowired
    TbItemCatMapper itemCatMapper;
    @Autowired
    TbItemMapper itemMapper;

    /**
     * 根据商品id获得商品详情
     *
     * @param goodsId
     * @return
     */
    @Override
    public ModelMap genItemHtml(Long goodsId) {
        ModelMap map = new ModelMap();
        TbGoods goods = tbGoodsMapper.selectByPrimaryKey(goodsId);
        TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(goodsId);
        map.put("goods", goods);
        map.put("goodsDesc", tbGoodsDesc);
        //3.读取商品分类
        String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
        String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
        String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
        map.put("itemCat1", itemCat1);
        map.put("itemCat2", itemCat2);
        map.put("itemCat3", itemCat3);

        //4.读取SKU列表
        TbItemExample example=new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);//SPU ID
        criteria.andStatusEqualTo("1");//状态有效
        example.setOrderByClause("is_default desc");//按是否默认字段进行降序排序，目的是返回的结果第一条为默认SKU
        List<TbItem> itemList = itemMapper.selectByExample(example);
        map.put("itemList", itemList);
        return map;
    }
}
