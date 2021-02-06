package cn.xiaofangwei.onlinesurvey.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("Answer")
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "answerId", type = IdType.AUTO)
    private Integer answerId;

    @TableField("questionnaireId")
    private Integer questionnaireId;

    @TableField("questionId")
    private Integer questionId;

    @TableField("submitId")
    private Integer submitId;

    @TableField("questionType")
    private Integer questionType;

    private Integer answer;

    @TableField("answerText")
    private String answerText;


}
