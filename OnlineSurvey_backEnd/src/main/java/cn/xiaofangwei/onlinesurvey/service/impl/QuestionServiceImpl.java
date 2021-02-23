package cn.xiaofangwei.onlinesurvey.service.impl;

import cn.xiaofangwei.onlinesurvey.entity.Option;
import cn.xiaofangwei.onlinesurvey.entity.Question;
import cn.xiaofangwei.onlinesurvey.mapper.OptionMapper;
import cn.xiaofangwei.onlinesurvey.mapper.QuestionMapper;
import cn.xiaofangwei.onlinesurvey.service.QuestionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Resource
    QuestionMapper questionMapper;

    @Resource
    OptionMapper optionMapper;

    @Override
    public Question selectQuestionWithOptions(Integer questionId) {
        Question question = questionMapper.selectById(questionId);
        QueryWrapper<Option> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionId", questionId);
        List<Option> options = optionMapper.selectList(queryWrapper);
        question.setOptions(options);
        return question;
    }

    @Override
    public List<Question> selectQuestionsWithOptions(Integer questionnaireId) {
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.eq("questionnaireId", questionnaireId);
        List<Question> questions = questionMapper.selectList(questionQueryWrapper);
        questions.forEach(question -> {
            QueryWrapper<Option> optionQueryWrapper = new QueryWrapper<>();
            optionQueryWrapper.eq("questionId", question.getQuestionId());
            List<Option> options = optionMapper.selectList(optionQueryWrapper);
            question.setOptions(options);
        });
        return questions;
    }
}
