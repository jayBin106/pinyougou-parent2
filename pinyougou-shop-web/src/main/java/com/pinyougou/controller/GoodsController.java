package com.pinyougou.controller;

import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.fastDFS.FastDFSClient;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.service.GoodsService;
import com.pinyougou.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @RequestMapping("/addGoods")
    public Result addGoods(@RequestBody Goods goods) {
        try {
            goodsService.addGoods(goods);
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
        return goodsService.findPage2(page, rows);
//		return goodsService.findPage(goods, page, rows);
    }

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

}
