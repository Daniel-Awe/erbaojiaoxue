package cn.edu.zju.temp.entity.uo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bytedeco.javacpp.presets.opencv_core;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/6/21 14:22
 * @version: 1.0
 */
@Data
public class QuestionSetUo {
    @Schema(description = "套题id")
    private Long id;

    @Schema(description = "套题名称")
    private String name;

    @Schema(description = "题目正确数量")
    private Integer questionRight;

    @Schema(description = "套题中题目总数")
    private Integer questionSum;

    @Schema(description = "准确率")
    private Double correctRate;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime editTime;

    @Schema(description = "考试时长")
    private Integer examDuration;

    @Schema(description = "考试设备")
    private String examDevice;

    @Schema(description = "已删除")
    private Boolean deleted;
}

