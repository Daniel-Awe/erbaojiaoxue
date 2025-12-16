package cn.edu.zju.temp.entity.uo;

import cn.edu.zju.temp.enums.PatientType;
import cn.edu.zju.temp.enums.SexType;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/14 16:15
 * @version: 1.0
 */
@Data
@ToString
public class PatientUo {

    @Schema(description = "病人id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "上传病例用户id")
    private Integer userId;

    @Schema(description = "病人姓名")
    private String name;

    @Schema(description = "性别")
    private SexType sex;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "超声号")
    private String ultrasonicNumber;

    @Schema(description = "仪器类型")
    private String instrumentType;

    @Schema(description = "门诊号")
    private String outpatientNumber;

    @Schema(description = "超声视频")
    private String ultrasonicVideo;

    @Schema(description = "检查项目")
    private String checkItem;

    @Schema(description = "病例类型")
    private PatientType type;

    @Schema(description = "异议数")
    private Integer objectionNum;

    @Schema(description = "切面准确率")
    private BigDecimal aspectCorrect;

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

    @Schema(description = "图片名称集合")
    private String ultsImgs;
}
