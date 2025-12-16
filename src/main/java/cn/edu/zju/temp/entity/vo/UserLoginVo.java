package cn.edu.zju.temp.entity.vo;

import cn.edu.zju.temp.entity.User;
import cn.edu.zju.temp.enums.UserType;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/4/30 16:25
 * @version: 1.0
 */

@Data
public class UserLoginVo {

    @Schema(description = "用户id")
    private Integer id;

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "学校")
    private String school;

    @Schema(description = "学院")
    private String institute;

    @Schema(description = "年级")
    private String grade;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "上传病例数")
    private Integer numUploadCases;

    @Schema(description = "病例切面正确率")
    private BigDecimal correctRateCase;

    @Schema(description = "题库正确率")
    private BigDecimal correctRateQuestionBank;

    @Schema(description = "题库完成率")
    private String questionBankCompleteRate;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime editTime;

    @Schema(description = "是否成功")
    private Boolean success;

    @Schema(description = "异常描述")
    private String errorMessage;

    @Schema(description = "登录凭证")
    private String token;

    @Schema(description = "登录者身份 ADMIN/USER")
    private UserType role;

    public static UserLoginVo fail(String errorMessage) {
        UserLoginVo vo = new UserLoginVo();
        vo.setSuccess(false);
        vo.setErrorMessage(errorMessage);
        return vo;
    }

    public static UserLoginVo success(User user, String token) {
        UserLoginVo vo = new UserLoginVo();
        BeanUtils.copyProperties(user, vo);
        vo.setToken(token);
        return vo;
    }

}
