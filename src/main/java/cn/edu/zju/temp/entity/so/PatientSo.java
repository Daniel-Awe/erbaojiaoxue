package cn.edu.zju.temp.entity.so;

import cn.edu.zju.temp.enums.PatientType;
import cn.edu.zju.temp.enums.SexType;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/7 9:14
 * @version: 1.0
 */
@Data
public class PatientSo {

    @Schema(description = "病人姓名")
    @NotNull(message = "病人姓名不能为空")
    private String name;

    @Schema(description = "上传病例用户id")
    @NotNull(message = "上传的用户id不能为空")
    private Integer userId;

    @Schema(description = "性别")
    @NotNull(message = "性别不能为空")
    private SexType sex;

    @Schema(description = "年龄")
    @NotNull(message = "年龄不能为空")
    private Integer age;

    @Schema(description = "超声号")
    private String ultrasonicNumber;

    @Schema(description = "仪器类型")
    private String instrumentType;

    @Schema(description = "门诊号")
    private String outpatientNumber;

    @Schema(description = "检查项目")
    private String checkItem;

    @Schema(description = "病例类型")
    private PatientType type;

    @Schema(description = "超声视频")
    private MultipartFile ultrasonicVideo;

    @Schema(description = "超声图片")
    private MultipartFile[] ultrasonicImages;

}
