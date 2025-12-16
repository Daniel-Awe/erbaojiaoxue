package cn.edu.zju.temp.entity.so;

import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/10 16:34
 * @version: 1.0
 */
@Data
@ToString
public class ResultObjectionSo {

    @Schema(description = "异议id")
    @NotNull
    private Long objectionId;

    @Schema(description = "处理结果 0未采纳 1采纳")
    @NotNull
    private Integer result;

    @Schema(description = "回复")
    private String reply;
}
