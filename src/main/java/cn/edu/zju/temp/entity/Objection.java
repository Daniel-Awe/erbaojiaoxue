package cn.edu.zju.temp.entity;

import cn.edu.zju.temp.enums.ObjectionType;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: 异议列表
 * @date: 2024/4/30 15:10
 * @version: 1.0
 */
@Getter
@Setter
@TableName("objection")
@Schema(name = "Objection", description = "异议列表")
public class Objection {
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "异议用户id")
    private Long userId;

    @Schema(description = "题目id")
    private Long questionId;

    @Schema(description = "病例id")
    private Long patientAnalyseId;

    @Schema(description = "套题id")
    private Long questionSetId;

    @Schema(description = "题目")
    private String img;

    @Schema(description = "答案")
    private String answer;

    @Schema(description = "异议答案")
    private String objectionAnswer;

    @Schema(description = "异议理由")
    private String objectionArgument;

    @Schema(description = "异议用户")
    private String objectionUser;

    @Schema(description = "处理状态 0未处理 1已处理")
    private Integer processingState;

    @Schema(description = "处理结果 0驳回 1采纳")
    private Integer processingResult;

    @Schema(description = "处理用户")
    private String processingUser;

    @Schema(description = "处理回复")
    private String processingReply;

    @Schema(description = "异议类型 病例/题库")
    private ObjectionType type;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime editTime;

    @Schema(description = "已删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

    public Objection() {
    }

    public Objection(Boolean deleted) {
        this.deleted = deleted;
    }
}
