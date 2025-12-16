package cn.edu.zju.temp.entity.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/10 8:40
 * @version: 1.0
 */
@Data
@ToString
public class ImgResponseSo {

    @Schema(description = "算法解析图片得到状态码")
    private Integer code;

    @Schema(description = "算法解析图片得到的答案等")
    private String data;

}
