package com.pinyougou.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.content.service.ContentCategoryService;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.fastDFS.FastDFSClient;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbContentCategory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/content")
public class ContentController {
    @Value("${fastDFS_URL}")
    private String fastDFSUrl;

    @Reference(version = "1.0.0")
    private ContentService contentService;
    @Reference(version = "1.0.0")
    private ContentCategoryService contentCategoryService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbContent> findAll() {
        return contentService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return contentService.findPage(page, rows);
    }

    /**
     * 增加
     *
     * @param content
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbContent content) {
        try {
            contentService.add(content);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param content
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbContent content) {
        try {
            contentService.update(content);
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
    public TbContent findOne(Long id) {
        return contentService.findOne(id);
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
            contentService.delete(ids);
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
    public PageResult search(@RequestBody TbContent content, int page, int rows) {
        return contentService.findPage(content, page, rows);
    }

    /**
     * 查询+分页
     *
     * @param brand
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/getContentCategoryList")
    public String getContentCategoryList() {
        List<TbContentCategory> tbContentCategoryList = contentCategoryService.findAll();
        List<Map<String, String>> maps = new ArrayList<>();
        for (TbContentCategory o : tbContentCategoryList) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", o.getId().toString());
            map.put("text", o.getName());
            maps.add(map);
        }
        String string = JSONObject.toJSONString(maps);
        return string;
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
}
