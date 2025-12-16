package cn.edu.zju.temp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/6 10:23
 * @version: 1.0
 */
public class Md5Encoder {

    public static String encode(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//加密密码
        String encode = passwordEncoder.encode(password);
        return encode;
    }

    public static boolean match(String password, String encode){
//验证密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(password, encode);
        if (matches){
            return true;
        }else {
            return false;
        }
    }

//    String md5Hex = DigestUtils.md5Hex("123456");
//        System.out.println(md5Hex);     //md5加密

//    String md5Crypt = Md5Crypt.md5Crypt("123456".getBytes(),"$1$12345678");
//        System.out.println(md5Crypt);  //md5盐值加密


//        最牛的BcryptPasswordEncoder密码加密  采用SHA-256 +随机盐+密钥对密码进行加密
//    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//    //  加密密码
//    String encode = passwordEncoder.encode("123456");
//    //  验证密码
//    boolean istrue = passwordEncoder.matches("123456", "$2a$10$IYTFwEHFBfOZRFqqQcAQeu.1OISGSuqFBPTDdSNMWPyLJcuzHhnl.");

}
