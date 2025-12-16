package cn.edu.zju.temp.entity.uo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/23 12:49
 * @version: 1.0
 */
@Data
public class OrderUo {

    @Schema(description = "字段名 例startTime")
    private String column;

    @Schema(description = "排序  1是升序  0是降序")
    private Integer order;

    @Schema(description = "要检索的姓名")
    private String name;

    @Schema(description = "套题id")
    private Long questionSetId;

    private Long page = 1L;
    private Long limit = 10L;
}
