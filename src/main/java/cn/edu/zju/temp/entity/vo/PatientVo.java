package cn.edu.zju.temp.entity.vo;

import cn.edu.zju.temp.enums.PatientType;
import cn.edu.zju.temp.enums.SexType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: 查询条件
 * @date: 2024/5/8 15:11
 * @version: 1.0
 */
@Data
public class PatientVo {

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

    private Integer userId;

    private Long page = 1L;

    private Long limit = 10L;
}
