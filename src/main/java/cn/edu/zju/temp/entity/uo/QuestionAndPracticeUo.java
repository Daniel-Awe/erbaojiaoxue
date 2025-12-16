package cn.edu.zju.temp.entity.uo;

import cn.edu.zju.temp.enums.QuestionType;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: TODO
 * @date: 2024/5/23 14:30
 * @version: 1.0
 */
@Data
@ToString
public class QuestionAndPracticeUo {

    @Schema(description = "题目id")
    private Long id;

    @Schema(description = "病例id")
    private Long patientAnalyseId;

    @Schema(description = "题目名称")
    private String name;

    @Schema(description = "题目答案")
    private String answer;

    @Schema(description = "已答题人数")
    private Integer answeredUser;

    @Schema(description = "正确人数")
    private Integer correctAnswered;

    @Schema(description = "正确率")
    private String correctRate;

    @Schema(description = "题目来源")
    private QuestionType topicSource;

    @Schema(description = "用户是否已经收藏")
    private Boolean isCollect;

    @Schema(description = "用户是否回答正确")
    private Boolean isTrue;

    @Schema(description = "用户是否提出异议")
    private Boolean isObjection;

    @Schema(description = "用户已经回答答案")
    private String userAnswer;

    @Schema(description = "切面答案%已提交/已驳回%reply")
    private String reply;

    @Schema(description = "创建人")
    private String createUser;

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
}
