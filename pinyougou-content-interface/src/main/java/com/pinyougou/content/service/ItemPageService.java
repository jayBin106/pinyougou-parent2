package com.pinyougou.content.service;

import org.springframework.ui.ModelMap;

/**
 * Created by lenovo on 2018/11/2.
 */
public interface ItemPageService {
    /**
     * 根据商品id获得商品详情
     *
     * @param goodsId
     * @return
     */
    public ModelMap genItemHtml(Long goodsId);
}
