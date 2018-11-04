package com.pinyougou.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.dao.TbBrandMapper;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lenovo on 2018/11/2.
 */
@Service(version = "2.0.0")
public class BrandServiceTwoImpl implements BrandService {
    @Autowired
    private TbBrandMapper tbBrandMapper;

    /**
     * 分页显示
     *
     * @param pageNum  页数
     * @param pageSize 页的大小
     * @return
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbBrand> tbBrands = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
        return new PageResult(tbBrands.getTotal(), tbBrands.getResult());
    }

    /**
     * 搜索 +  分页
     *
     * @param tbBrand
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(TbBrand tbBrand, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        TbBrandExample tbBrandExample = new TbBrandExample();
        TbBrandExample.Criteria criteria = tbBrandExample.createCriteria();
        if (tbBrand.getName() != null && tbBrand.getName().length() > 0) {
            criteria.andNameLike("%" + tbBrand.getName() + "%");
        } else if (tbBrand.getFirstChar() != null && tbBrand.getFirstChar().length() > 0) {
            criteria.andFirstCharEqualTo(tbBrand.getFirstChar());
        }
        Page<TbBrand> tbBrands = (Page<TbBrand>) tbBrandMapper.selectByExample(tbBrandExample);
        return new PageResult(tbBrands.getTotal(), tbBrands.getResult());
    }

    /**
     * 新增品牌
     *
     * @param tbBrand
     * @return
     */
    @Override
    public Result add(TbBrand tbBrand) {
        int insert = tbBrandMapper.insert(tbBrand);
        return insert > 0 ? new Result() : new Result(false, "400");
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Result delete(long[] id) {
        try {
            for (long l : id) {
                int i = tbBrandMapper.deleteByPrimaryKey(l);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new Result();
    }

    /**
     * 更新
     *
     * @param tbBrand
     * @return
     */
    @Override
    public Result update(TbBrand tbBrand) {
        int i = tbBrandMapper.updateByPrimaryKey(tbBrand);
        return i > 0 ? new Result() : new Result(false, "400");
    }

    /**
     * 查询实体
     *
     * @param id
     * @return
     */
    @Override
    public TbBrand selectOne(long id) {
        TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(id);
        return tbBrand;
    }
}
