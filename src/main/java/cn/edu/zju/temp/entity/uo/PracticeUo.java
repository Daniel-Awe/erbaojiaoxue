package cn.edu.zju.temp.entity.uo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/23 14:35
 * @version: 1.0
 */
@Data
public class PracticeUo {

    @Schema(description = "用户id")
    private Integer userId;
    @Schema(description = "套题id")
    private Long questionSetId;
    private Long page = 1L;
    private Long limit = 10L;
}
