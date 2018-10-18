package com.pinyougou.service;

import com.pinyougou.entity.Result;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    Result imagesUpload(MultipartFile multipartFile);
}
