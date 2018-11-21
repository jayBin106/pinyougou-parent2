package com.pinyougou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.entity.Result;
import com.pinyougou.order.service.AddressService;
import com.pinyougou.pojo.TbAddress;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojoGroup.Cart;
import com.pinyougou.until.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/10/25.
 */
@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference(version = "1.0.0",timeout =66666)
    private ContentService contentService;
    @Reference(version = "1.0.0",timeout = 66666)
    private AddressService addressService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;


    /**
     * 根据广告分类ID查询广告列表
     *
     * @param categoryId
     * @return
     */
    @RequestMapping("/findByCategoryId")
    public List<TbContent> findByCategoryId(Long categoryId) {
        List<TbContent> byCategoryId = contentService.findByCategoryId(categoryId);
        return byCategoryId;
    }

    /**
     * 添加购物车
     *
     * @param
     * @return
     */
    @RequestMapping("/addCart")
    public Result addCart(String itemId, String num) {
        try {
//            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            //购物车列表
            List<Cart> cartList = findCartList();
            List<Cart> carts = contentService.addGoodsToCartList(cartList, itemId, Integer.valueOf(num));
            //购物车数据存入cookies
            CookieUtil.setCookie(request, response, "cartList", JSONObject.toJSONString(carts), 3600 * 24, "UTF-8");
            System.out.println(carts);
            return new Result();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Result("添加购物车失败");
        }
    }

    /**
     * 查询购物车列表
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {
        String cartList = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (StringUtils.isNotEmpty(cartList)) {
            List<Cart> carts = JSONArray.parseArray(cartList, Cart.class);
            return carts;
        }
        return new ArrayList<Cart>();
    }

    /**
     * 收获地址获取
     */
    @RequestMapping("/getAdressList")
    public List<TbAddress> getAdressList() {
        List<TbAddress> tbAddresses = addressService.findAll();
        return tbAddresses;
    }

}
