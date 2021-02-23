package cn.xiaofangwei.onlinesurvey.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

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
@TableName("Question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "questionId", type = IdType.AUTO)
    private Integer questionId;

    @TableField("questionnaireId")
    private Integer questionnaireId;

    @TableField("questionTitle")
    private String questionTitle;

    @TableField("questionType")
    private Integer questionType;

    private Integer required;

    private Integer row;

    @TableField(exist = false)
    private List<Option> options;


}
