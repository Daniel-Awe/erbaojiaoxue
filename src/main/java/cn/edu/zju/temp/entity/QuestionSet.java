package cn.edu.zju.temp.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/6/17 9:26
 * @version: 1.0
 */
@Getter
@Setter
@TableName("question_set")
@Schema(name = "QuestionSet", description = "套题集合")
public class QuestionSet {

    @Schema(description = "套题id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "套题名称")
    private String name;

    @Schema(description = "套题正确率")
    private String correctRate;

    @Schema(description = "套题完成率")
    private String completeRate;

    @Schema(description = "套题正确人数")
    private Integer correctAnswer;

    @Schema(description = "套题回答人数")
    private Integer answeredUser;

    @Schema(description = "套题中题目总数")
    private Integer questionSum;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime editTime;

    @Schema(description = "考试时长")
    private Integer examDuration;

    @Schema(description = "考试设备")
    private String examDevice;


    @Schema(description = "已删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

}
