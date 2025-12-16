package cn.edu.zju.temp.entity.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/27 13:51
 * @version: 1.0
 */
@Data
public class ObjectionTo {
    @Schema(description = "用户id 可传可不传")
    private Integer userId;

    @Schema(description = "0全部 1未处理 2已处理")
    private Integer state;

    private Long page = 1L;
    private Long limit = 10L;
}
