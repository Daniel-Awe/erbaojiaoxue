package cn.edu.zju.temp.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class JsonReader {
    public static JSONObject readJSON(String jsonPath) {
        File jsonFile = new File(jsonPath);
        if (!jsonFile.exists()) {
            return null;
        }
        try {
            String jsonStr = new String(Files.readAllBytes(jsonFile.toPath()));
            return JSON.parseObject(jsonStr);
        } catch (IOException e) {
            log.error("读取json文件失败", e);
            return null;
        }
    }
}
