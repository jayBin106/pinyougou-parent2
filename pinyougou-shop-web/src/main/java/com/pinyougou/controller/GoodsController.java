package com.pinyougou.controller;

import com.alibaba.fastjson.JSONObject;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.fastDFS.FastDFSClient;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.service.GoodsService;
import com.pinyougou.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Value("${fastDFS_URL}")
    private String fastDFSUrl;

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UploadFileService uploadFileService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return goodsService.findPage(page, rows);
    }

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage2")
    public PageResult findPage2(int page, int rows) {
        return goodsService.findPage2(page, rows);
    }

    /**
     * 增加
     *
     * @param goods
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbGoods goods) {
        try {
            goodsService.add(goods);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 增加
     *
     * @param goods
     * @return
     */
    @RequestMapping("/saveGoods")
    public Result saveGoods(@RequestBody String goods) {
        try {
            System.out.println("商品集合---" + goods);
            Goods parseObject = JSONObject.parseObject(goods, Goods.class);
            //获取当前登录的商家ID
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            //之前商品的商家ID
            String sellerId1 = goodsService.findGoods(parseObject.getTbGoods().getId()).getTbGoods().getSellerId();
            if (parseObject.getTbGoods().getId() != null) {
                //如果传递过来的商家ID并不是当前登录的用户的ID,则属于非法操作
                if (!sellerId1.equals(sellerId) || !parseObject.getTbGoods().getSellerId().equals(sellerId)) {
                    return new Result(false, "操作非法");
                }
                goodsService.updateGoods(parseObject);
            } else {
                parseObject.getTbGoods().setSellerId(sellerId);
                goodsService.addGoods(parseObject);
            }
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbGoods goods) {
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
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbGoods findOne(Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
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
     *
     * @param brand
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
//        return goodsService.findPage2(page, rows);
        return goodsService.findPage(goods, page, rows);
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @RequestMapping("/uploadFile")
    @ResponseBody
    public Result uploadFile(MultipartFile file) {
//        Result result = uploadFileService.imagesUpload(file);
//        return result;
        Result result = new Result();
        try {
            //1、取文件的扩展名
            String filename = file.getOriginalFilename();
            String lastName = filename.substring(filename.lastIndexOf("."));
            //2、创建一个FastDFS的客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fastDFSClient.properties");
            //3、执行上传处理
            String path = fastDFSClient.uploadFile(file.getBytes(), lastName);
            //4、拼接返回的url和ip地址，拼装成完整的url
            String url = fastDFSUrl + path;
            result.setMessage(url);
            return result;
        } catch (Exception e) {
            return new Result("文件上传出现异常，上传失败");
        }
    }

    /**
     * 更新状态
     *
     * @param sign   1，更新审核状态2，上下架状态
     * @param ids
     * @param status
     * @return
     */
    @Transactional
    @RequestMapping("/updateStatus")
    public Result updateStatus(String sign, Long[] ids, String status) {
        if ("1".equals(sign)) {
            for (Long id : ids) {
                TbGoods tbGoods = new TbGoods();
                tbGoods.setId(id);
                tbGoods.setAuditStatus(status);
                goodsService.updateByPrimaryKeySelective(tbGoods);
            }
        } else if ("2".equals(sign)) {
            for (Long id : ids) {
                TbGoods tbGoods = new TbGoods();
                tbGoods.setId(id);
                tbGoods.setIsMarketable(status);
                goodsService.updateByPrimaryKeySelective(tbGoods);
            }
        }
        //注解事物测试
//        String[] strings={};
//        String a=strings[4];
        return new Result();
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findGoods")
    public Goods findGoods(Long id) {
        return goodsService.findGoods(id);
    }
}
