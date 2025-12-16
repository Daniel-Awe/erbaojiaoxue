package cn.edu.zju.temp.entity;

import cn.edu.zju.temp.enums.QuestionType;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: 题库表
 * @date: 2024/4/30 14:58
 * @version: 1.0
 */
@Getter
@Setter
@TableName("question_bank")
@Schema(name = "Question", description = "题库")
public class Question {
    @Schema(description = "题目id")
    @TableId(value = "id", type = IdType.AUTO)
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

    @Schema(description = "创建人")
    private String createUser;

    @Schema(description = "套题id")
    private Long questionSetId;

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

    public Question() {
    }

    public Question(String name, String answer, Boolean deleted) {
        this.name = name;
        this.answer = answer;
        this.deleted = deleted;
    }

    public Question(Long id, String answer) {
        this.id = id;
        this.answer = answer;
    }
}
