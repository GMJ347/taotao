package cn.gmj.taotao.upload.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    public String imageUpload(MultipartFile file);
}
