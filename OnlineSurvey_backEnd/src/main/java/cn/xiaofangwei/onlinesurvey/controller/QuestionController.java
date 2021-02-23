package cn.xiaofangwei.onlinesurvey.controller;


import cn.xiaofangwei.onlinesurvey.entity.Message;
import cn.xiaofangwei.onlinesurvey.entity.Question;
import cn.xiaofangwei.onlinesurvey.service.OptionService;
import cn.xiaofangwei.onlinesurvey.service.QuestionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */
@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final OptionService optionService;

    @Autowired
    public QuestionController(QuestionService questionService, OptionService optionService) {
        this.questionService = questionService;
        this.optionService = optionService;
    }

    @GetMapping
    public Message getQuestion(@RequestParam("questionId")Integer questionId) throws SQLException {
        Question question = questionService.selectQuestionWithOptions(questionId);
        return Message.info(question);
    }

    @GetMapping("/list")
    public Message getQuestionListByQuestionnaireId(@RequestParam("questionnaireId")Integer questionnaireId) throws SQLException {
        List<Question> questions = questionService.selectQuestionsWithOptions(questionnaireId);
        return Message.info(questions);
    }

    @PostMapping
    public Message saveQuestion(Question question) throws SQLException {
        questionService.saveOrUpdate(question);
        optionService.saveOrUpdateBatch(question.getOptions());
        return Message.info();
    }

    @PutMapping
    public Message updateQuestion(Question question) throws SQLException {
        questionService.updateById(question);
        return Message.info();
    }

    @DeleteMapping
    public Message deleteQuestion(@RequestParam("questionId")Integer questionId) throws SQLException {
        questionService.removeById(questionId);
        return Message.info();
    }
}
