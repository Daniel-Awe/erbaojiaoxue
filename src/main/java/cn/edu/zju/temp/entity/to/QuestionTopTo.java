package cn.edu.zju.temp.entity.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/21 17:25
 * @version: 1.0
 */
@Data
public class QuestionTopTo {

    @Schema(description = "未处理异议数")
    private String unResolveNums;

    @Schema(description = "题目数")
    private Integer questionNum;

    @Schema(description = "正确率")
    private String correctRate;

    @Schema(description = "完成率")
    private String completeRate;
}
