package cn.xiaofangwei.onlinesurvey.service.impl;

import cn.xiaofangwei.onlinesurvey.entity.Question;
import cn.xiaofangwei.onlinesurvey.entity.Questionnaire;
import cn.xiaofangwei.onlinesurvey.mapper.QuestionMapper;
import cn.xiaofangwei.onlinesurvey.mapper.QuestionnaireMapper;
import cn.xiaofangwei.onlinesurvey.service.QuestionnaireService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */
@Service
@Transactional
public class QuestionnaireServiceImpl extends ServiceImpl<QuestionnaireMapper, Questionnaire> implements QuestionnaireService {

    @Resource
    QuestionnaireMapper questionnaireMapper;
    @Resource
    QuestionMapper questionMapper;

    @Override
    public IPage<Map<String, Object>> selectMapsPage(Page questionnairePage, QueryWrapper wrapper) {
        return questionnaireMapper.selectMapsPage(questionnairePage, wrapper);
    }

    @Override
    public Questionnaire selectQuestionnaireWithQuestion(Integer questionnaireId) {
        Questionnaire questionnaire = questionnaireMapper.selectById(questionnaireId);
        List<Question> questions = questionMapper.selectQuestionsWithOptions(questionnaireId);
        questionnaire.setQuestions(questions);
        return questionnaire;
    }
}
