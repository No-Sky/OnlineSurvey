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
@TableName("User_Tag")
public class UserTag {
    private static final long serialVersionUID = 1L;

    @TableId(value = "utId", type = IdType.AUTO)
    private Integer utId;

    private Integer userId;
}

