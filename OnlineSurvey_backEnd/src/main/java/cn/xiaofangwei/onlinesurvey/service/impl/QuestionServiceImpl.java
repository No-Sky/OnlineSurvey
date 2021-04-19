package cn.xiaofangwei.onlinesurvey.service.impl;

import cn.xiaofangwei.onlinesurvey.entity.Answer;
import cn.xiaofangwei.onlinesurvey.entity.Option;
import cn.xiaofangwei.onlinesurvey.entity.Question;
import cn.xiaofangwei.onlinesurvey.entity.SubmitInfo;
import cn.xiaofangwei.onlinesurvey.mapper.AnswerMapper;
import cn.xiaofangwei.onlinesurvey.mapper.OptionMapper;
import cn.xiaofangwei.onlinesurvey.mapper.QuestionMapper;
import cn.xiaofangwei.onlinesurvey.mapper.SubmitInfoMapper;
import cn.xiaofangwei.onlinesurvey.service.QuestionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
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
@Transactional(rollbackFor=Exception.class)
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Resource
    QuestionMapper questionMapper;

    @Resource
    OptionMapper optionMapper;

    @Resource
    SubmitInfoMapper submitInfoMapper;

    @Resource
    AnswerMapper answerMapper;

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

    @Override
    public List<Question> selectQuestionWithOptionsWithAnswer(Integer questionnaireId, Integer userId) {
        List<Question> questions = selectQuestionsWithOptions(questionnaireId);
        QueryWrapper<SubmitInfo> w = new QueryWrapper<>();
        w.eq("userId", userId);
        w.eq("questionnaireId", questionnaireId);
        SubmitInfo submitInfo = submitInfoMapper.selectOne(w);
        for (Question question : questions) {
            QueryWrapper<Answer> wrapper = new QueryWrapper<>();
            wrapper.eq("submitId", submitInfo.getSubmitId());
            wrapper.eq("questionId", question.getQuestionId());
            List<Answer> answers = answerMapper.selectList(wrapper);
            System.out.println(answers.toString());
            if (question.getQuestionType() == 1) question.setRadioValue(answers.get(0).getAnswer());
            else if (question.getQuestionType() == 2) {
                List<Integer> checkValue = new ArrayList<>();
                answers.forEach(answer -> {
                    checkValue.add(answer.getAnswer());
                });
                Integer[] ckv = new Integer[checkValue.size()];
                for (int i = 0; i < checkValue.size(); i++) {
                    ckv[i] = checkValue.get(i);
                }
                question.setCheckboxValue(ckv);
            } else if (question.getQuestionType() == 3) {
                question.setTextValue(answers.get(0).getAnswerText());
            }
        }
        return questions;
    }
}
