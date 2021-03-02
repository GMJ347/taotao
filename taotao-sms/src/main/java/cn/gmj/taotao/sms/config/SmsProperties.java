package cn.gmj.taotao.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "taotao.sms")
public class SmsProperties {
    // accessKeyId
    public String accessKeyId;
    // AccessKeySecret
    public String accessKeySecret;
    // 签名名称
    public String signName;
    // 模板名称
    public String verifyCodeTemplate;
}
