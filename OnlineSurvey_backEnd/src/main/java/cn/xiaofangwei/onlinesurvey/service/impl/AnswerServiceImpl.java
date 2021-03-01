package cn.xiaofangwei.onlinesurvey.service.impl;

import cn.xiaofangwei.onlinesurvey.entity.Answer;
import cn.xiaofangwei.onlinesurvey.entity.Question;
import cn.xiaofangwei.onlinesurvey.entity.Questionnaire;
import cn.xiaofangwei.onlinesurvey.entity.SubmitInfo;
import cn.xiaofangwei.onlinesurvey.entity.vo.AnalysisSelectView;
import cn.xiaofangwei.onlinesurvey.entity.vo.DataAnalysis;
import cn.xiaofangwei.onlinesurvey.mapper.AnswerMapper;
import cn.xiaofangwei.onlinesurvey.mapper.QuestionMapper;
import cn.xiaofangwei.onlinesurvey.mapper.SubmitInfoMapper;
import cn.xiaofangwei.onlinesurvey.service.AnswerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {

    @Resource
    AnswerMapper answerMapper;
    @Resource
    SubmitInfoMapper submitInfoMapper;
    @Resource
    QuestionMapper questionMapper;


    @Override
    public void saveAnswers(Integer questionnaireId, List<Question> questions, Integer useTime, String ip) throws SQLException {
        System.out.println(questions.toString());
        System.out.println(useTime);
        SubmitInfo submitInfo = new SubmitInfo();
        submitInfo.setQuestionnaireId(questionnaireId);
        submitInfo.setUseTime(useTime);
        submitInfo.setSubmitIP(ip);
        submitInfo.setSubmitTime(LocalDateTime.now());
        submitInfoMapper.insert(submitInfo);
        List<Answer> answers = new ArrayList<>();
        questions.forEach(question -> {
            if (question.getQuestionType() == 1) {
                Answer answer = new Answer();
                answer.setSubmitId(submitInfo.getSubmitId());
                answer.setQuestionnaireId(question.getQuestionnaireId());
                answer.setQuestionId(question.getQuestionId());
                answer.setQuestionType(1);
                answer.setAnswer(question.getRadioValue());
                answers.add(answer);
            } else if (question.getQuestionType() == 2) {
                for (Integer checkboxValue : question.getCheckboxValue()) {
                    Answer answer = new Answer();
                    answer.setSubmitId(submitInfo.getSubmitId());
                    answer.setQuestionnaireId(question.getQuestionnaireId());
                    answer.setQuestionId(question.getQuestionId());
                    answer.setQuestionType(2);
                    answer.setAnswer(checkboxValue);
                    answers.add(answer);
                }
            } else if (question.getQuestionType() == 3) {
                Answer answer = new Answer();
                answer.setSubmitId(submitInfo.getSubmitId());
                answer.setQuestionnaireId(question.getQuestionnaireId());
                answer.setQuestionId(question.getQuestionId());
                answer.setQuestionType(3);
                answer.setAnswerText(question.getTextValue());
                answers.add(answer);
            }
        });
        saveBatch(answers);
    }

    @Override
    public List<DataAnalysis> getDataAnalysis(Integer questionnaireId) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionnaireId", questionnaireId);
        List<Question> questions = questionMapper.selectList(queryWrapper);
        List<DataAnalysis> analysisList = new ArrayList<>();
        questions.forEach(question -> {
            DataAnalysis analysis = new DataAnalysis();
            analysis.setQuestionId(question.getQuestionId());
            analysis.setQuestionTitle(question.getQuestionTitle());
            analysis.setQuestionType(question.getQuestionType());
            if (question.getQuestionType() == 1 || question.getQuestionType() == 2) {
                analysis.setAnalysisResult(getQuestionAnanlysis(question.getQuestionId()));
            }
            analysisList.add(analysis);
        });
        return analysisList;
    }

    @Override
    public IPage<Map<String, Object>> selectMapsPage(Page answerPage, QueryWrapper wrapper) {
        return answerMapper.selectMapsPage(answerPage, wrapper);
    }

    private List<Map<String, Object>> getQuestionAnanlysis(Integer questionId) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<AnalysisSelectView> selectViews = answerMapper.selectOptionAnalysis(questionId);
        Integer total= 0;
        for(AnalysisSelectView a : selectViews) {
            total += a.getSubmitCount();
        }
        int percent = 0;
        for(AnalysisSelectView a : selectViews) {
            if (total == 0) percent = 0;
            else percent=((int) (a.getSubmitCount()/total)*10000)/100;
            Map<String, Object> map = new HashMap<>();
            map.put("option", a.getContent());
            map.put("count", a.getSubmitCount());
            map.put("percent", percent);
            result.add(map);
        }
        return result;
    }
}
