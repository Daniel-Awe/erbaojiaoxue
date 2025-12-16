package cn.edu.zju.temp.entity.uo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: gsq
 * @description: TODO  未处理以及已处理状态 排序
 * @date: 2024/5/27 16:56
 * @version: 1.0
 */
@Data
public class OrderPatientStateUo {
    @Schema(description = "字段名 例startTime")
    private String column;

    @Schema(description = "排序  1是升序  0是降序")
    private Integer order;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "state异议查询  0是查全部 1是未处理 2是已处理")
    private Integer state;

    private Long page = 1L;
    private Long limit = 10L;
}
