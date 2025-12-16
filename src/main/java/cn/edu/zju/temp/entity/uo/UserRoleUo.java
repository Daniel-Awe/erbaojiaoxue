package cn.edu.zju.temp.entity.uo;

import cn.edu.zju.temp.enums.UserType;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/6/6 10:43
 * @version: 1.0
 */
@Data
public class UserRoleUo {
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

    @Schema(description = "用户身份 admin管理员 user用户")
    private UserType role;

    @Schema(description = "上传病例数")
    private Integer numUploadCases;

    @Schema(description = "病例切面正确率")
    private String correctRateCase;

    @Schema(description = "题库正确率")
    private String correctRateQuestionBank;

    @Schema(description = "题库完成率")
    private String questionBankCompleteRate;

    @Schema(description = "上次登陆时间")
    private LocalDateTime lastLogin;

    @Schema(description = "是否删除 0未删除 1删除")
    private Boolean isDeleted;

    private Integer page;
    private Integer limit;
}
