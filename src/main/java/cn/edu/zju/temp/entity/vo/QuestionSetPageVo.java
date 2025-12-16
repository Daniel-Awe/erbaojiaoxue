package cn.edu.zju.temp.entity.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/6/21 10:22
 * @version: 1.0
 */
@Data
public class QuestionSetPageVo {
    @Schema(description = "套题id")
    private Long id;

    @Schema(description = "套题名称")
    private String name;

    @Schema(description = "套题正确率")
    private BigDecimal correctRate;

    @Schema(description = "套题正确人数")
    private Integer correctAnswer;

    @Schema(description = "套题回答人数")
    private Integer answeredUser;

    @Schema(description = "套题中题目总数")
    private Integer questionSum;

    @Schema(description = "错题数量/收藏数量")
    private Integer errors;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime editTime;

    @Schema(description = "已删除")
    private Boolean deleted;

}
