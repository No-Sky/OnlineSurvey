package cn.xiaofangwei.onlinesurvey.controller;


import cn.xiaofangwei.onlinesurvey.entity.Message;
import cn.xiaofangwei.onlinesurvey.entity.Questionnaire;
import cn.xiaofangwei.onlinesurvey.service.QuestionnaireService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;

    @Autowired
    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @GetMapping
    public Message getQuestionnaireById(@RequestParam("questionnaireId")Integer questionnaireId) throws SQLException {
        Questionnaire questionnaire = questionnaireService.getById(questionnaireId);
        return Message.info(questionnaire);
    }

    @GetMapping("/list")
    public Message getQuestionnaireList(@RequestParam("page")Integer page) throws SQLException{
        //分页条件
        QueryWrapper<Questionnaire> wrapper = new QueryWrapper<>();
        //分页序列，1：页面号，2：大小
        Page<Questionnaire> questionnairePage = new Page<>(page,10);
        IPage<Map<String, Object>> mapIPage = questionnaireService.selectMapsPage(questionnairePage, wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("pages", mapIPage.getPages());
        result.put("total", mapIPage.getTotal());
        result.put("records", mapIPage.getRecords());
        return Message.info("页面获取成功", result);
    }

    @GetMapping("/user")
    public Message getQuestionnaireListByUser(@RequestParam("userId")Integer userId) throws SQLException {
        QueryWrapper<Questionnaire> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<Questionnaire> questionnaireList = questionnaireService.list(queryWrapper);
        return Message.info(questionnaireList);
    }

    @PostMapping
    public Message saveQuestionnaire(Questionnaire questionnaire) throws SQLException {
        questionnaireService.save(questionnaire);
        return Message.info();
    }

    @PutMapping
    public Message updateQuestionnaire(Questionnaire questionnaire) throws SQLException {
        questionnaireService.updateById(questionnaire);
        return Message.info();
    }

    @DeleteMapping
    public Message deleteQuestionnaireById(@RequestParam("questionnaireId")Integer questionnaireId) throws SQLException {
        questionnaireService.removeById(questionnaireId);
        return Message.info();
    }

}
