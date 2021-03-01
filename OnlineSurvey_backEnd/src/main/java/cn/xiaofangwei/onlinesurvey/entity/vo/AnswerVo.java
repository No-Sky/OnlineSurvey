package cn.xiaofangwei.onlinesurvey.entity.vo;

import cn.xiaofangwei.onlinesurvey.entity.Question;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class AnswerVo {

    private Integer questionnaireId;

    private List<Question> questions;

    private Integer useTime;


}
