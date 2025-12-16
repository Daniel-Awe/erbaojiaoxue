package cn.edu.zju.temp.util;

import cn.edu.zju.temp.config.LocalEnv;
import cn.edu.zju.temp.config.OssUploadConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 工具类上传图片url路径
 */
@Slf4j
@Component
public class CommonUpload {

//    @Value("${ossimg.path}")
//    private String basePath;

    @Autowired
    private LocalEnv localEnv;

    @Autowired
    private AliOSSUtils aliOSSUtils;

//    private String basePath = "E:/erbaojiaoxue";

    public String uploadimg(MultipartFile file){
        log.info("当前上传的文件是{}",file.toString());

//先上传到图片到oss上
        String url = null;
        try {
            url = aliOSSUtils.upload(file);
            log.info(url);

//  得到原始文件名
            String originalFilename = file.getOriginalFilename();

//  得到.jpg
            String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

//  利用UUID，防止文件重名
            String filename = originalFilename;

//  判断目录是否存在，不存在则创建出目录
            File dir = new File(localEnv.getStaticPath() + localEnv.getOssimgPath());
            if(!dir.exists()){
                dir.mkdir();
            }

            file.transferTo(new File(localEnv.getStaticPath() + localEnv.getOssimgPath() +filename));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return url;
    }


    /**
     * 根据路径将图片显示在浏览器端
     * @param name
     * @param response
     */
    public void download(String name, HttpServletResponse response) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(localEnv.getStaticPath() + localEnv.getOssimgPath() + name));
            ServletOutputStream outputStream = response.getOutputStream();

//  固定写法，返回给浏览器的是图片文件
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            len = fileInputStream.read(bytes);
            while (len != -1){
                outputStream.write(bytes,0,len);
                len = fileInputStream.read(bytes);
                outputStream.flush();
            }

            fileInputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}