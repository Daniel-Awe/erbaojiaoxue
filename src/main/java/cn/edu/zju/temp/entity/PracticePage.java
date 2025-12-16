package cn.edu.zju.temp.entity;

import cn.edu.zju.temp.enums.QuestionType;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author: gsq
 * @description: 错题/收藏题
 * @date: 2024/4/30 15:44
 * @version: 1.0
 */
@Getter
@Setter
@TableName("practice_page")
@Schema(name = "PracticePage", description = "错题和收藏列表")
public class PracticePage {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "题目id")
    private Long questionId;

    @Schema(description = "套题id")
    private Long questionSetId;

    @Schema(description = "错题名称")
    private String name;

    @Schema(description = "题目正确答案")
    private String answer;

    @Schema(description = "用户填写答案")
    private String userAnswer;

    @Schema(description = "是否有异议")
    private Boolean isObjection;

    @Schema(description = "是否正确")
    private Boolean isTrue;

    @Schema(description = "是否收藏")
    private Boolean isCollect;

    @Schema(description = "题目来源")
    private QuestionType topicSource;

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

    public PracticePage(Boolean deleted) {
        this.deleted = deleted;
    }

    public PracticePage() {
    }
}
