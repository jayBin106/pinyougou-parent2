package com.pinyougou.service;

import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbBrand;

/**
 * Created by lenovo on 2018/9/26.
 */
public interface BrandService {

    /**
     * 分页显示
     *
     * @param pageNum  页数
     * @param pageSize 页的大小
     * @return
     */
    public PageResult findPage(int pageNum, int pageSize);

    /**
     * 搜索 +  分页
     *
     * @param tbBrand
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult findPage(TbBrand tbBrand, int pageNum, int pageSize);

    /**
     * 新增品牌
     *
     * @param tbBrand
     * @return
     */
    public Result add(TbBrand tbBrand);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public Result delete(long[] id);

    /**
     * 更新
     *
     * @param tbBrand
     * @return
     */
    public Result update(TbBrand tbBrand);

    /**
     * 查询实体
     *
     * @param id
     * @return
     */
    public TbBrand selectOne(long id);


}
