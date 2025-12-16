package cn.edu.zju.temp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/7 9:57
 * @version: 1.0
 */
@Component
@Data
@ConfigurationProperties("oss.upload")
public class OssUploadConfig {

    /**
     * 节点
     */
    private String endpoint;

    /**
     * 身份id
     */
    private String accessKeyId;

    /**
     * 密钥
     */
    private String accessKeySecret;

    /**
     * 存储位置名
     */
    private String bucketName;


    /**
     * 存放目录
     */
    private String folderName;
}
