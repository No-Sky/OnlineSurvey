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
@TableName("Option")
public class Option implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "optionId", type = IdType.AUTO)
    private Integer optionId;

    @TableField("questionId")
    private Integer questionId;

    private String content;


}
