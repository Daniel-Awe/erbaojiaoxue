package cn.edu.zju.temp.entity.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/21 13:43
 * @version: 1.0
 */
@Data
public class UserTopTo {

    @Schema(description = "今日上传病例数")
    private Integer todayUploadCaseNum;

    @Schema(description = "病例总数")
    private Integer caseTotal;

    @Schema(description = "未处理异议数")
    private Integer unResolveCase;

    @Schema(description = "切面正确率")
    private String aspectCorrectRate;

    @Schema(description = "今日答题总数")
    private Integer todayAnswerNum;

    @Schema(description = "异议数")
    private Integer objectionNum;

    @Schema(description = "题库正确率")
    private String questionCorrect;

    @Schema(description = "题库完成率")
    private String questionComplete;
}
