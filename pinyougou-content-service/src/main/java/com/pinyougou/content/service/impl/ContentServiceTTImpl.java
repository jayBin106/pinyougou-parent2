package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojoGroup.Cart;

import java.util.List;

/**
 * ·ContentServiceTTImpl
 * ·李文彬
 * 2018/11/24 ·18:25
 */
@Service(version = "1.0.0")
public class ContentServiceTTImpl implements ContentService {
    @Override
    public List<TbContent> findAll() {
        return null;
    }

    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        return null;
    }

    @Override
    public void add(TbContent content) {

    }

    @Override
    public void update(TbContent content) {

    }

    @Override
    public TbContent findOne(Long id) {
        return null;
    }

    @Override
    public void delete(Long[] ids) {

    }

    @Override
    public PageResult findPage(TbContent content, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public List<TbContent> findByCategoryId(Long categoryId) {
        return null;
    }

    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, String itemId, Integer num) {
        return null;
    }

    @Override
    public List<Cart> findByCategoryId(String cartName) {
        return null;
    }

    @Override
    public void redisSaveCart(String userId, List<Cart> cartList) {

    }
}
