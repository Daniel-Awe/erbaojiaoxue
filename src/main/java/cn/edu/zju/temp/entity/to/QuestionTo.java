package cn.edu.zju.temp.entity.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: gsq
 * @description: zip压缩包导入需要的to类
 * @date: 2024/5/13 15:24
 * @version: 1.0
 */
@Data
public class QuestionTo {

    @Schema(description = "xlsx文件id")
    private Long id;

    @Schema(description = "题目名称 是图片名称")
    private String name;

    @Schema(description = "题目答案")
    private String answer;

    @Schema(description = "超声号")
    private String ultraCode;

    @Schema(description = "阴性/阳性")
    private String nature;
}
