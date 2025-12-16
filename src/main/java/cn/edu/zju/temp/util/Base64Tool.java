package cn.edu.zju.temp.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

@Slf4j
public class Base64Tool {

    public static String readImageToBase64(String imagePath) {
        if (StringUtils.isBlank(imagePath)) {
            return null;
        }
        File file = new File(imagePath);
        if (!file.exists()) {
            return null;
        }
        String fileName = file.getName();
        int idx = fileName.lastIndexOf(".");
        if (idx == -1) {
            log.error("文件不是图片");
            return null;
        }
        String suffix = fileName.substring(idx + 1);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BufferedImage bufferedImage = ImageIO.read(file);
            ImageIO.write(bufferedImage, suffix, outputStream);
            byte[] bytes = outputStream.toByteArray();
            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            log.error("读取图片失败", e);
        }
        return null;
    }

}
