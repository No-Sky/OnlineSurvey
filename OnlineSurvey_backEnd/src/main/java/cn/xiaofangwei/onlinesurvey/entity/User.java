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
@TableName("User")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "userId", type = IdType.AUTO)
    private Integer userId;

    private String email;

    private String password;

    private String username;

    @TableField("statusType")
    private Integer statusType;

    private Integer score;

}
