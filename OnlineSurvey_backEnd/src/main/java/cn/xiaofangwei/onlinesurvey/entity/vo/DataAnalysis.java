package cn.xiaofangwei.onlinesurvey.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class DataAnalysis {
    private Integer questionId;
    private String questionTitle;
    private Integer questionType;
    private List<Map<String, Object>> analysisResult;

}
