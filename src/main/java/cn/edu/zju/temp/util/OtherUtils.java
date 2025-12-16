package cn.edu.zju.temp.util;

import org.springframework.util.StringUtils;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/14 13:37
 * @version: 1.0
 */
public class OtherUtils {

//文件名 去掉前面的目录部分
    private String delMulu(String filename1) {
        String[] split ;
        String photoName = null;
        if (filename1.contains("/")){
            split = filename1.split("/");
            photoName = split[split.length-1];
        }else if (filename1.contains("\\")){
            split = filename1.split("\\\\");
            photoName = split[split.length-1];
        }else {
            photoName = filename1;
        }
        return photoName;
    }

//  去掉目录的文件名，在去掉后缀名
    private static String getIdCard(String photoName) {
        String idCard = photoName.substring(0, photoName.indexOf("."));
        System.out.println(" 文件名："+ photoName);
        if (StringUtils.isEmpty(photoName)){
            throw new RuntimeException("获取文件名错误");
        }
        return idCard;
    }

// 得到后缀名
    private static String getSubString(String filename){
        String substring = filename.substring(filename.lastIndexOf("."));
        return substring;
    }
}
