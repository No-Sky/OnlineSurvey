package cn.xiaofangwei.onlinesurvey.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AnalysisSelectView {
    private Integer optionId;
    private Integer submitCount;
    private String content;
}
