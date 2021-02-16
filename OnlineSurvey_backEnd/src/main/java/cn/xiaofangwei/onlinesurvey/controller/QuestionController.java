package cn.xiaofangwei.onlinesurvey.controller;


import cn.xiaofangwei.onlinesurvey.entity.Message;
import cn.xiaofangwei.onlinesurvey.entity.Question;
import cn.xiaofangwei.onlinesurvey.service.QuestionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Delete;
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

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public Message getQuestion(@RequestParam("questionId")Integer questionId) throws SQLException {
        Question question = questionService.getById(questionId);
        return Message.info(question);
    }

    @GetMapping("/list")
    public Message getQuestionListByQuestionnaireId(@RequestParam("questionnaireId")Integer questionnaireId) throws SQLException {
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.eq("questionnaireId", questionnaireId);
        List<Object> questions = questionService.listObjs(questionQueryWrapper);
        return Message.info(questions);
    }

    @PostMapping
    public Message saveQuestion(Question question) throws SQLException {
        questionService.save(question);
        return Message.info();
    }

    @PutMapping
    public Message updateQuestion(Question question) throws SQLException {
        questionService.updateById(question);
        return Message.info();
    }

    @DeleteMapping
    public Message deleteQuestion(@RequestParam("questionId")Integer questionId) {
        questionService.removeById(questionId);
        return Message.info();
    }
}
