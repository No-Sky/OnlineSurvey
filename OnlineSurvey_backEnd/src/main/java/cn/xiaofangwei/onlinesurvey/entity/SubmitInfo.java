package cn.xiaofangwei.onlinesurvey.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
@TableName("SubmitInfo")
public class SubmitInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "submitId", type = IdType.AUTO)
    private Integer submitId;

    @TableField("questionnaireId")
    private Integer questionnaireId;

    @TableField("userId")
    private Integer userId;

    @TableField("submitIP")
    private String submitIP;

    @TableField("submitTime")
    private LocalDateTime submitTime;

    @TableField("useTime")
    private Integer useTime;


}
