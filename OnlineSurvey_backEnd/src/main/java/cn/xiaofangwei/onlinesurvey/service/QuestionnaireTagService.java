package cn.xiaofangwei.onlinesurvey.service;

import cn.xiaofangwei.onlinesurvey.entity.QuestionnaireTag;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionnaireTagService extends IService<QuestionnaireTag> {
    List<QuestionnaireTag> getTagsByQId(Integer questionnaireId);
}
