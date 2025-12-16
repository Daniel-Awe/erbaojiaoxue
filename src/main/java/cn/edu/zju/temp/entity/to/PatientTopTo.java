package cn.edu.zju.temp.entity.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/21 17:42
 * @version: 1.0
 */
@Data
public class PatientTopTo {

    @Schema(description = "今日新增病例")
    private Integer todayCase;

    @Schema(description = "病例总数")
    private Integer caseTotal;

    @Schema(description = "异议总数")
    private Integer objectionNum;

    @Schema(description = "切面准确率")
    private String aspectRate;
}
