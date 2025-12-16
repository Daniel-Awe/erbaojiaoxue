package cn.edu.zju.temp.entity.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/21 17:50
 * @version: 1.0
 */
@Data
public class PracticeErrorTopTo {

    @Schema(description = "今日错题")
    private Integer todayError;

    @Schema(description = "错题总数")
    private Integer totalSum;

    @Schema(description = "正确率")
    private String correctRate;

    @Schema(description = "完成率")
    private String completaRate;

}
