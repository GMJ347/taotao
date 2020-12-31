package cn.gmj.taotao.upload.service.impl;

import cn.gmj.taotao.upload.config.UploadProperties;
import cn.gmj.taotao.upload.controller.UploadController;
import cn.gmj.taotao.upload.service.UploadService;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Service
@EnableConfigurationProperties(UploadProperties.class)
public class UploadServiceImpl implements UploadService {

    @Autowired private FastFileStorageClient fastFileStorageClient;
    @Autowired private UploadProperties prop;
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Override
    public String imageUpload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        // 校验文件类型
        String contentType = file.getContentType();
        if (!prop.getAllowTypes().contains(contentType)) {
            logger.info("上传失败，文件类型不匹配：{}", contentType);
            return null;
        }
        // 校验文件内容
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                logger.info("上传失败，文件内容不合法。");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // 保存到FastDFS
        //  String extension = filename.substring(filename.lastIndexOf(".")+1);
        String extension = StringUtils.substringAfterLast(filename, ".");
        try {
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            return prop.getBaseUrl() + storePath.getFullPath();
        } catch (IOException e) {
            logger.error("上传文件失败！", e);
            e.printStackTrace();
        }

        return null;
    }
}
