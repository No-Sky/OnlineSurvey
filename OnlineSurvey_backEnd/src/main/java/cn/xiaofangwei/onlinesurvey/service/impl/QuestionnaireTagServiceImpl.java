package cn.xiaofangwei.onlinesurvey.service.impl;

import cn.xiaofangwei.onlinesurvey.entity.QuestionnaireTag;
import cn.xiaofangwei.onlinesurvey.mapper.QuestionnaireTagMapper;
import cn.xiaofangwei.onlinesurvey.service.QuestionnaireTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class QuestionnaireTagServiceImpl extends ServiceImpl<QuestionnaireTagMapper, QuestionnaireTag> implements QuestionnaireTagService {

    @Resource
    QuestionnaireTagMapper questionnaireTagMapper;


    @Override
    public List<QuestionnaireTag> getTagsByQId(Integer questionnaireId) {
        return questionnaireTagMapper.selectTagsByQuestionnaire(questionnaireId);
    }
}
