package cn.gmj.taotao.upload.service.impl;

import cn.gmj.taotao.upload.controller.UploadController;
import cn.gmj.taotao.upload.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Service
public class UploadServiceImpl implements UploadService {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private static final List<String> IMAGE_TYPE = Arrays.asList("image/jpeg", "image/gif", "image/png", "image/bmp");

    @Override
    public String imageUpload(MultipartFile file) {
        // 校验文件类型
        String contentType = file.getContentType();
        if (!IMAGE_TYPE.contains(contentType)) {
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

        // 保存到本地
        try {
            file.transferTo(new File("F:\\taotao\\image\\"+file.getOriginalFilename()));
            return "http://image.taota.com/" + file.getOriginalFilename();
        } catch (IOException e) {
            logger.info("服务器内部错误");
            e.printStackTrace();
            return null;
        }
    }
}
