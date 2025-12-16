package cn.edu.zju.temp.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/6/19 9:38
 * @version: 1.0
 */
@Data
public class CaseIdAndSetIdVo {

    @Schema(description = "病例分析图id")
    private Long[] ids;
    @Schema(description = "套题id")
    private Long questionSetId;
}
