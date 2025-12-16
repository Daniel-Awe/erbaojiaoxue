package cn.edu.zju.temp.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/7 13:41
 * @version: 1.0
 */
@Data
public class QuestionVo {

    @Schema(description = "题目id")
    private Long id;

    @Schema(description = "题目名称图片")
    private MultipartFile name;

    @Schema(description = "题目答案")
    @NotNull(message = "题目答案不能为空")
    private String answer;
}
