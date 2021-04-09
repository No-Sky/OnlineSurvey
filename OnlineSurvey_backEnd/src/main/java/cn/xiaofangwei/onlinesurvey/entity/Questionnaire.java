package cn.xiaofangwei.onlinesurvey.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("Questionnaire")
public class Questionnaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "questionnaireId", type = IdType.AUTO)
    private Integer questionnaireId;

    @TableField("userId")
    private Integer userId;

    private String title;

    private String description;

    @TableField("statusType")
    private Integer statusType;

    @TableField("createTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createTime;

    @TableField("stopTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime stopTime;

    private Integer distribution;

    @TableField("deleteFlag")
    private Boolean deleteFlag;

    @TableField("isPublic")
    public Boolean isPublic;

    @TableField(exist = false)
    private List<Question> questions;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private List<QuestionnaireTag> tags;


}
