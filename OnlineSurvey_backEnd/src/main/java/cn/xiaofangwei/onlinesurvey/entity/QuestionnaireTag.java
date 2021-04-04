package cn.xiaofangwei.onlinesurvey.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("Questionnaire_Tag")
public class QuestionnaireTag {
    private static final long serialVersionUID = 1L;

    @TableId(value = "qtId", type = IdType.AUTO)
    private Integer qtId;

    private Integer questionnaireId;
}
