package com.pinyougou.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.dao.*;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.*;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service(value = "goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbBrandMapper brandMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbSellerMapper sellerMapper;


    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbGoods goods) {
        goodsMapper.insert(goods);
    }

    /**
     * 增加
     */
    @Override
    public void addGoods(Goods goods) {
        goods.getTbGoods().setAuditStatus("0");//设置未申请状态
        goodsMapper.insert(goods.getTbGoods());
        goods.getTbGoodsDesc().setGoodsId(goods.getTbGoods().getId());
        tbGoodsDescMapper.insert(goods.getTbGoodsDesc());

        for (TbItem item : goods.getItemList()) {
            //标题
            String title = goods.getTbGoods().getGoodsName();
            Map<String, Object> specMap = JSON.parseObject(item.getSpec());
            for (String key : specMap.keySet()) {
                title += " " + specMap.get(key);
            }
            item.setTitle(title);
            item.setGoodsId(goods.getTbGoods().getId());//商品SPU编号
            item.setSellerId(goods.getTbGoods().getSellerId());//商家编号
            item.setCategoryid(goods.getTbGoods().getCategory3Id());//商品分类编号（3级）
            item.setCreateTime(new Date());//创建日期
            item.setUpdateTime(new Date());//修改日期
            //品牌名称
            TbBrand brand = brandMapper.selectByPrimaryKey(goods.getTbGoods().getBrandId());
            item.setBrand(brand.getName());
            //分类名称
            TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getTbGoods().getCategory3Id());
            item.setCategory(itemCat.getName());
            //商家名称
            TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getTbGoods().getSellerId());
            item.setSeller(seller.getNickName());
            //图片地址（取spu的第一个图片）
            List<Map> imageList = JSON.parseArray(goods.getTbGoodsDesc().getItemImages(), Map.class);
            if (imageList.size() > 0) {
                item.setImage((String) imageList.get(0).get("url"));
            }
            itemMapper.insert(item);
        }
    }


    /**
     * 修改
     */
    @Override
    public void update(TbGoods goods) {
        goodsMapper.updateByPrimaryKey(goods);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbGoods findOne(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            goodsMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();

        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 返回分页列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage2(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Map> page = (Page<Map>) goodsMapper.selectGoodsPage();
        return new PageResult(page.getTotal(), page.getResult());
    }
}
