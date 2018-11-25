package com.pinyougou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
	@Reference(version = "1.0.0")
	private GoodsService goodsService;
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){
		return goodsService.findAll();
	}


	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows){

		return goodsService.findPage(page, rows);
	}

	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbGoods goods){
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}

	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbGoods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}

	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbGoods findOne(Long id){
		return goodsService.findOne(id);
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}

		/**
	 * 查询+分页
	 * @param brand
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);
	}

	/**
	 * 更新状态
	 *
	 * @param sign   1，更新审核状态2，上下架状态
	 *               商品审核通过后，才可以上架
	 * @param ids
	 * @param status
	 * @return
	 */
	@Transactional
	@RequestMapping("/updateStatus")
	public Result updateStatus(String sign, Long[] ids, String status) {
		TbGoods tbGoods = new TbGoods();
		//商品审核
		if ("1".equals(sign)) {
			for (Long id : ids) {
				tbGoods.setId(id);
				tbGoods.setAuditStatus(status);
				goodsService.updateByPrimaryKeySelective(tbGoods);
			}
		} else if ("2".equals(sign)) {
			//商品上下架
			for (Long id : ids) {
				tbGoods.setId(id);
				tbGoods.setIsMarketable(status);
				goodsService.updateByPrimaryKeySelective(tbGoods);
				//商品上架后，调用mq把商品加入solr库中
				tbGoods = goodsService.findOne(id);
				String goodsStr = JSONObject.toJSONString(tbGoods);
				jmsMessagingTemplate.convertAndSend("goodsStr",goodsStr);
			}
		}
		//注解事物测试
//        String[] strings={};
//        String a=strings[4];
		return new Result();
	}
}
