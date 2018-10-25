package com.pinyougou.serviceImpl;

import com.pinyougou.entity.Result;
import com.pinyougou.fastDFS.FastDFSClient;
import com.pinyougou.service.UploadFileService;
import org.springframework.beans.factory.annotation.Value;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements UploadFileService {
    @Value("${fastDFS_URL}")
    private String fastDFSUrl;

    /**
     * fastDFS上传文件的方式
     *
     * @param file
     * @return
     */
    public Result imagesUpload(MultipartFile file) {
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
