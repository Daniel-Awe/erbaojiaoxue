package cn.edu.zju.temp.entity.so;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/9 14:10
 * @version: 1.0
 */
@Data
public class ObjectionSo {

    @Schema(description = "用户id")
    @NotNull
    private Long userId;

    @Schema(description = "题目id")
    private Long questionId;

    @Schema(description = "病例分析id")
    private Long patientAnalyseId;

    @Schema(description = "套题id")
    private Long questionSetId;

    @Schema(description = "异议答案")
    @NotNull
    private String objectionAnswer;

    @Schema(description = "异议理由")
    @NotNull
    private String objectionArgument;

}
