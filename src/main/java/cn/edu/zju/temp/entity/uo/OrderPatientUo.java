package cn.edu.zju.temp.entity.uo;

import cn.edu.zju.temp.enums.PatientType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/24 12:46
 * @version: 1.0
 */
@Data
public class OrderPatientUo {
    @Schema(description = "字段名 例startTime")
    private String column;

    @Schema(description = "排序  1是升序  0是降序")
    private Integer order;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "病人姓名")
    private String name;

    @Schema(description = "超声号")
    private String ultrasonicNumber;

    @Schema(description = "病例类型")
    private PatientType type;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "病人id")
    private Long PatientId;

    private Long page = 1L;
    private Long limit = 10L;
}
