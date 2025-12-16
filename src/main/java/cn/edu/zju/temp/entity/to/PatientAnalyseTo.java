package cn.edu.zju.temp.entity.to;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/22 15:36
 * @version: 1.0
 */
@Data
public class PatientAnalyseTo {
    @Schema(description = "病人id")
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

    @Schema(description = "概率")
    private double possibility;

    @Schema(description = "是否异议")
    private Boolean isObjection;

    @Schema(description = "切面答案%已提交/已驳回%reply")
    private String reply;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime editTime;

    @Schema(description = "已删除")
    private Boolean deleted;
}
