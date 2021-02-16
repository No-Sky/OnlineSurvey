package cn.xiaofangwei.onlinesurvey.controller;


import cn.xiaofangwei.onlinesurvey.entity.Answer;
import cn.xiaofangwei.onlinesurvey.entity.Message;
import cn.xiaofangwei.onlinesurvey.service.AnswerService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

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

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping
    public Message getAnswer(@RequestParam("answerId")Integer answerId) throws SQLException {
        Answer answer = answerService.getById(answerId);
        return Message.info(answer);
    }

    @PostMapping
    public Message saveAnswer(Answer answer) throws SQLException {
        answerService.save(answer);
        return Message.info();
    }

    @PutMapping
    public Message updateAnswer(Answer answer) throws SQLException {
        answerService.updateById(answer);
        return Message.info();
    }

    @DeleteMapping
    public Message deleteAnswer(@RequestParam("amswerId")Integer answerId) {
        answerService.removeById(answerId);
        return Message.info();
    }
}
