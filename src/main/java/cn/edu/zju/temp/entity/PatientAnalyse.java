package cn.edu.zju.temp.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: 病例分析表
 * @date: 2024/4/30 15:36
 * @version: 1.0
 */
@Getter
@Setter
@TableName("patient_analyse")
@Schema(name = "PatientAnalyse", description = "病例分析表")
public class PatientAnalyse {

    @Schema(description = "病人id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "病人id")
    private Integer patientId;

    @Schema(description = "原图片")
    private String originalImg;

    @Schema(description = "分析图/帧")
    private String analyseImg;

    @Schema(description = "分析答案")
    private String analyseAnswer;

    @Schema(description = "切面名称")
    private String aspectName;

    @Schema(description = "是否标准答案")
    private Boolean isStandard;

    @Schema(description = "概率")
    private Double possibility;

    @Schema(description = "是否异议")
    private Boolean isObjection;

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

    public PatientAnalyse() {
    }

    public PatientAnalyse(Integer patientId, String originalImg,Boolean deleted) {
        this.patientId = patientId;
        this.originalImg = originalImg;
        this.deleted = deleted;
    }
}
