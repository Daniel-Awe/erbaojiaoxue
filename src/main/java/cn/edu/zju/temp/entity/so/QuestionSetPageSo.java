package cn.edu.zju.temp.entity.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bytedeco.javacpp.presets.opencv_core;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/6/21 11:33
 * @version: 1.0
 */
@Data
public class QuestionSetPageSo {

    @Schema(description = "套题id")
    private Long questionSetId;

    private Long page = 1L;

    private Long limit = 10L;

    private Integer userId;
}
