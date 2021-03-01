package cn.xiaofangwei.onlinesurvey.controller;


import cn.xiaofangwei.onlinesurvey.entity.Answer;
import cn.xiaofangwei.onlinesurvey.entity.Message;
import cn.xiaofangwei.onlinesurvey.entity.Question;
import cn.xiaofangwei.onlinesurvey.entity.Questionnaire;
import cn.xiaofangwei.onlinesurvey.entity.vo.AnswerVo;
import cn.xiaofangwei.onlinesurvey.entity.vo.DataAnalysis;
import cn.xiaofangwei.onlinesurvey.service.AnswerService;
import cn.xiaofangwei.onlinesurvey.service.QuestionnaireService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */
@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionnaireService questionnaireService;

    @Autowired
    public AnswerController(AnswerService answerService, QuestionnaireService questionnaireService) {
        this.answerService = answerService;
        this.questionnaireService = questionnaireService;
    }

    @GetMapping
    public Message getAnswer(@RequestParam("answerId")Integer answerId) throws SQLException {
        Answer answer = answerService.getById(answerId);
        return Message.info(answer);
    }

    @GetMapping("/analysis")
    public Message getAnswerAnalysis(@RequestParam("questionnaireId")Integer questionnaireId) {
        List<DataAnalysis> analysis = answerService.getDataAnalysis(questionnaireId);
        return Message.info(analysis);
    }

    @GetMapping("/text")
    public Message getAnswerTextAnalysis(@RequestParam("questionId") Integer questionId, @RequestParam("currentPage") Integer page,
                                         @RequestParam("pageSize") Integer pageSize) {
        Page<Answer> answerPage = new Page<>(page, pageSize);
        QueryWrapper<Questionnaire> wrapper = new QueryWrapper<>();
        wrapper.eq("questionId", questionId);
        IPage<Map<String, Object>> mapIPage = answerService.selectMapsPage(answerPage, wrapper);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("texts", mapIPage.getRecords());
        resultMap.put("total", mapIPage.getTotal());
        return Message.info(resultMap);
    }

    @PostMapping
    public Message saveAnswer(@RequestBody AnswerVo answerVo, HttpServletRequest request) throws SQLException {
        Integer questionnaireId = answerVo.getQuestionnaireId();
        Integer statusType = questionnaireService.getById(questionnaireId).getStatusType();
        if (statusType == 0) return Message.error("请先发布问卷！");
        Integer useTime = answerVo.getUseTime();
        List<Question> questions = answerVo.getQuestions();
        String ip = request.getRemoteAddr();
        answerService.saveAnswers(questionnaireId, questions, useTime, ip);
        return Message.info();
    }

    @PutMapping
    public Message updateAnswer(Answer answer) throws SQLException {
        answerService.updateById(answer);
        return Message.info();
    }

    @DeleteMapping
    public Message deleteAnswer(@RequestParam("amswerId")Integer answerId) throws SQLException {
        answerService.removeById(answerId);
        return Message.info();
    }

}
