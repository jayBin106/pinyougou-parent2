package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.dao.TbContentMapper;
import com.pinyougou.dao.TbItemMapper;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbContentExample;
import com.pinyougou.pojo.TbContentExample.Criteria;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojoGroup.Cart;
import com.pinyougou.until.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务实现层
 *
 * @author Administrator
 */
//@Service(version = "1.0.0")
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TbItemMapper tbItemMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbContent> findAll() {
        return contentMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbContent content) {
        contentMapper.insert(content);
        redisUtil.del("findByCategoryId");
    }


    /**
     * 修改
     */
    @Override
    public void update(TbContent content) {
        contentMapper.updateByPrimaryKey(content);
        redisUtil.del("findByCategoryId");
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbContent findOne(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            contentMapper.deleteByPrimaryKey(id);
        }
    }


    /**
     * 根据广告分类ID查询广告列表
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<TbContent> findByCategoryId(Long categoryId) {
        Object findByCategoryId = redisUtil.getMap("findByCategoryId:" + categoryId, categoryId + "");
        if (findByCategoryId != null) {
            List<TbContent> objects = JSONArray.parseArray(findByCategoryId.toString(), TbContent.class);
            return objects;
        } else {
            TbContentExample example = new TbContentExample();
            Criteria criteria = example.createCriteria();
            criteria.andCategoryIdEqualTo(categoryId);
            criteria.andStatusEqualTo("1");
            example.setOrderByClause("sort_order");//排序
            List<TbContent> tbContents = contentMapper.selectByExample(example);
            String jsonString = JSONObject.toJSONString(tbContents);
            redisUtil.setMap("findByCategoryId:" + categoryId, categoryId + "", jsonString);
            return tbContents;
        }
    }

    @Override
    public PageResult findPage(TbContent content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();

        if (content != null) {
            if (content.getTitle() != null && content.getTitle().length() > 0) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
            if (content.getUrl() != null && content.getUrl().length() > 0) {
                criteria.andUrlLike("%" + content.getUrl() + "%");
            }
            if (content.getPic() != null && content.getPic().length() > 0) {
                criteria.andPicLike("%" + content.getPic() + "%");
            }
            if (content.getStatus() != null && content.getStatus().length() > 0) {
                criteria.andStatusLike("%" + content.getStatus() + "%");
            }

        }

        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增购物车
     *
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, String itemId, Integer num) {
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(Long.valueOf(itemId));
        if (tbItem == null) {
            throw new RuntimeException("商品不存在！！");
        }
        if (!tbItem.getStatus().equals("1")) {
            throw new RuntimeException("商品不存在！！");
        }
        //获得商家id
        String sellerId = tbItem.getSellerId();
        //3.根据商家ID判断购物车列表中是否存在该商家的购物车
        Cart cart = null;
        if (cartList != null && cartList.size() > 0) {
            for (Cart c : cartList) {
                if (c.getSellerId().equals(sellerId)) {
                    cart = c;
                }
            }
        }
        //如果购物车列表不存在该商家的购物车
        if (cart == null) {
            Cart cart1 = new Cart();
            cart1.setSellerId(sellerId);
            cart1.setSellerName(tbItem.getSeller());
            List<TbOrderItem> orderItem = createOrderItem(tbItem, num);
            cart1.setOrderItemList(orderItem);
            //将购物车对象加入购物车列表
            cartList.add(cart1);
        } else {
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            boolean bl = true;
            for (TbOrderItem tbOrderItem : orderItemList) {
                Long itemId1 = tbOrderItem.getItemId();
                if ((itemId1 + "").equals(itemId)) {
                    int totalNum = tbOrderItem.getNum() + num;
                    //设置数量
                    tbOrderItem.setNum(totalNum);
                    //设置金额
                    tbOrderItem.setTotalFee(new BigDecimal(tbOrderItem.getPrice().doubleValue() * totalNum));
                    bl = false;
                }
            }
            //判断该商家列表中，是否有该商品明细
            // 如果没有，新增购物车明细
            if (bl) {
                List<TbOrderItem> orderItem = createOrderItem(tbItem, num);
                orderItemList.addAll(orderItem);
            }
        }
        return cartList;
    }

    private Cart searchCartBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            boolean equals = cart.getSellerId().equals(sellerId);
            if (equals) {
                return cart;
            }
        }
        return null;
    }

    /**
     * 创建订单明细
     *
     * @param item
     * @param num
     * @return
     */
    private List<TbOrderItem> createOrderItem(TbItem item, Integer num) {
        if (num <= 0) {
            throw new RuntimeException("数量非法");
        }
        List<TbOrderItem> orderItems = new ArrayList<TbOrderItem>();
        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setItemId(item.getId());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTitle(item.getTitle());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));
        orderItems.add(orderItem);
        return orderItems;
    }

    /**
     * redis获取购物车信息
     *
     * @param cartName
     */
    @Override
    public List<Cart> findByCategoryId(String cartName) {
        String s = redisUtil.get(cartName);
        List<Cart> carts = JSONArray.parseArray(s, Cart.class);
        return carts;
    }

    /**
     * 购物车信息存入redis
     *
     * @param cartName
     * @param cartList
     */
    @Override
    public void redisSaveCart(String cartName, List<Cart> cartList) {
        String jsonString = JSONObject.toJSONString(cartList);
        redisUtil.set(cartName, jsonString);
    }
}
