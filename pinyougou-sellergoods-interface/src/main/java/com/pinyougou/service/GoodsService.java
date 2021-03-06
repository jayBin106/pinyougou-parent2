package com.pinyougou.service;

import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.TbGoods;

import java.util.List;

/**
 * 服务层接口
 *
 * @author Administrator
 */
public interface GoodsService {

    /**
     * 返回全部列表
     *
     * @return
     */
    public List<TbGoods> findAll();


    /**
     * 返回分页列表
     *
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);

    /**
     * 返回分页列表
     *
     * @return
     */
    public PageResult findPage2(int pageNum, int pageSize);


    /**
     * 增加
     */
    public void add(TbGoods goods);

    /**
     * 增加
     */
    public void addGoods(Goods goods);

    //更新
    public void updateGoods(Goods goods);

    /**
     * 修改
     */
    public void update(TbGoods goods);

    public void updateByPrimaryKeySelective(TbGoods goods);


    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    public TbGoods findOne(Long id);


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
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize);

    /**
     * 查询good集合
     *
     * @param id
     * @return
     */
    public Goods findGoods(Long id);
}
