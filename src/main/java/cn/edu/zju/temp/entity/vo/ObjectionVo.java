package cn.edu.zju.temp.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/17 15:00
 * @version: 1.0
 */
@Data
public class ObjectionVo {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "当前页")
    private Long page = 1L;

    @Schema(description = "每页数据")
    private Long limit = 10L;
}
