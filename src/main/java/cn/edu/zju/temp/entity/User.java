package cn.edu.zju.temp.entity;

import cn.edu.zju.temp.enums.UserType;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: 用户列表
 * @date: 2024/4/30 14:24
 * @version: 1.0
 */
@Getter
@Setter
@TableName("user")
@Schema(name = "User", description = "用户列表")
public class User {

    @Schema(description = "用户id")
    @TableId(value = "id", type = IdType.AUTO)
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

    @Schema(description = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime editTime;

    @Schema(description = "已删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.deleted = false;
    }

    public User() {
    }
}
