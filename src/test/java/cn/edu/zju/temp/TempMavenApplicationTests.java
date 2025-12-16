package cn.edu.zju.temp;

import cn.edu.zju.temp.config.LocalEnv;
import cn.edu.zju.temp.entity.so.ImgInfoSo;
import cn.edu.zju.temp.entity.so.ImgResponseSo;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class TempMavenApplicationTests {

	@Test
	public void testSuanfa() throws IOException {
//		OkHttpClient client = new OkHttpClient().newBuilder()
//				.build();
//		MediaType mediaType = MediaType.parse("text/plain");
//		RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
//				.addFormDataPart("image_name","data1.jpg")
//				.addFormDataPart("file","E:\\xungengyiyi.jpg",
//						RequestBody.create(MediaType.parse("application/octet-stream"),
//								new File("E:\\xungengyiyi.jpg")))
//				.build();
//		Request request = new Request.Builder()
//				.url("http://183.129.170.180:6448/erbao/v1/prediction")
//				.method("POST", body)
//				.addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
//				.build();
//		Response response = client.newCall(request).execute();
	}

	@Test
	public void testSuanfa2(){

		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.get("multipart/form-data; charset=utf-8");
		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("file", "file.jpg",
						RequestBody.create(MediaType.parse("application/octet-stream"),
								new File("E:\\xungengyiyi.jpg")))
				.build();

		Request request = new Request.Builder()
				.url("http://183.129.170.180:6448/erbao/v1/prediction")
				.header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
				.post(requestBody)
				.build();

		try {
			Response response = client.newCall(request).execute();
			String body = response.body().string();
			ImgResponseSo imgResponseSo = JSON.parseObject(body, ImgResponseSo.class);
			String data = imgResponseSo.getData();
			List<ImgInfoSo> imgInfoSo = JSON.parseArray(data, ImgInfoSo.class);
			System.out.println(imgInfoSo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
