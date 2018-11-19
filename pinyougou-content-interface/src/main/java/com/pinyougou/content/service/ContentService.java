package com.pinyougou.content.service;

import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojoGroup.Cart;

import java.util.List;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface ContentService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbContent> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);


    /**
     * 增加
     */
    public void add(TbContent content);


    /**
     * 修改
     */
    public void update(TbContent content);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public TbContent findOne(Long id);


    /**
     * 批量删除
     *
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 分页
     *
     * @param pageNum  当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    public PageResult findPage(TbContent content, int pageNum, int pageSize);

    /**
     * 根据广告分类ID查询广告列表
     *
     * @param categoryId
     * @return
     */
    List<TbContent> findByCategoryId(Long categoryId);

    /**
     * 新增购物车
     *
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
    public List<Cart> addGoodsToCartList(List<Cart> cartList, String itemId, Integer num);

    /**
     * redis获取购物车信息
     */
    public List<Cart> findByCategoryId(String cartName);

    /**
     * 购物车信息存入redis
     *
     * @param cartName
     * @param cartList
     */
    public void redisSaveCart(String cartName, List<Cart> cartList);
}
