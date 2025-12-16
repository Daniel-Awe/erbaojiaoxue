package cn.edu.zju.temp.util;

import cn.edu.zju.temp.entity.so.ImgInfoSo;
import cn.edu.zju.temp.entity.so.ImgResponseSo;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import okio.BufferedSink;
import okio.Okio;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/10 10:05
 * @version: 1.0
 */
public class QuestionAnalyse {
    public static ImgInfoSo getAnswerByfile(File file) throws IOException {
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
////                .addFormDataPart("image_name",file.getName())
//                .addFormDataPart("file", file.getName(),
//                        RequestBody.create(MediaType.parse("multipart/form-data"),
//                                file))
//                .build();
//        Request request = new Request.Builder()
//                .url("http://183.129.170.180:6448/erbao/v1/prediction")
//                .method("POST", body)
//                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//            String body1 = response.body().string();
//            if (body1 == null || body1.equals("Internal Server Error")){
//                throw new RuntimeException("算法检测失败");
//            }
//            ImgResponseSo imgResponseSo = JSON.parseObject(body1, ImgResponseSo.class);
//            String data = imgResponseSo.getData();
//            List<ImgInfoSo> imgInfoSo = JSON.parseArray(data, ImgInfoSo.class);
//            System.out.println(imgInfoSo);
//            ImgInfoSo infoSo = imgInfoSo.get(0);
//            return infoSo;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;

        ImgInfoSo imgInfoSo = new ImgInfoSo();
        ArrayList<Double> doubles = new ArrayList<>();
        doubles.add(0.1);
        doubles.add(0.2);
        doubles.add(0.3);
        doubles.add(0.4);
        doubles.add(0.5);
        doubles.add(0.6);
        imgInfoSo.setLabel("切面1");
        imgInfoSo.setConf(doubles);
        imgInfoSo.setImg_name("a");
        return imgInfoSo;
    }
}
