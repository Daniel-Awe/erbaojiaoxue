package cn.edu.zju.temp.entity.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/6 16:21
 * @version: 1.0
 */
@Data
public class QuestionSo {

    @Schema(description = "题目名称图片")
    @NotNull(message = "题目名不能为空")
    private MultipartFile name;

    @Schema(description = "题目答案")
    @NotNull(message = "题目答案不能为空")
    private String answer;

    @Schema(description = "套题id")
    @NotNull
    private Long questionSetId;
}
