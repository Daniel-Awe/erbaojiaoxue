package cn.edu.zju.temp.entity.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/10 8:43
 * @version: 1.0
 */
@Data
@ToString
public class ImgInfoSo {

    @Schema(description = "图片类型")
    private String img_name;

    @Schema(description = "配置信息")
    private List<Double> conf;

    @Schema(description = "切面")
    private String label;
}
