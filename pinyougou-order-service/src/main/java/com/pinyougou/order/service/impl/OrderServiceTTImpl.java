package com.pinyougou.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.entity.PageResult;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbOrder;

import java.util.List;

/**
 * Created by lenovo on 2018/11/21.
 */
@Service(version = "3.0.0")
public class OrderServiceTTImpl implements OrderService {
    /**
     * 返回全部列表
     *
     * @return
     */
    @Override
    public List<TbOrder> findAll() {
        return null;
    }

    /**
     * 返回分页列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        return null;
    }

    /**
     * 增加
     *
     * @param order
     */
    @Override
    public void add(TbOrder order) {

    }

    /**
     * 修改
     *
     * @param order
     */
    @Override
    public void update(TbOrder order) {

    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbOrder findOne(Long id) {
        return null;
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {

    }

    /**
     * 分页
     *
     * @param order
     * @param pageNum  当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    @Override
    public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
        return null;
    }
}
