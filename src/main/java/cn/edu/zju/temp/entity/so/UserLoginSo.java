package cn.edu.zju.temp.entity.so;

import cn.edu.zju.temp.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: xjw
 * @description: TODO
 * @date: 2024/4/30 16:41
 * @version: 1.0
 */
@Data
public class UserLoginSo {

    @Schema(description = "用户名")
    @NotNull(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码")
    @NotNull(message = "密码不能为空")
    private String password;

    @Schema(description = "用户身份")
    private UserType role;

    @Schema(description = "用户性别")
    private String gender;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "学校")
    private String school;

    @Schema(description = "学院")
    private String institute;

    @Schema(description = "年级")
    private String grade;
}
