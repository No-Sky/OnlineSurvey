package cn.xiaofangwei.onlinesurvey.controller;


import cn.xiaofangwei.onlinesurvey.entity.*;
import cn.xiaofangwei.onlinesurvey.entity.vo.AnswerVo;
import cn.xiaofangwei.onlinesurvey.entity.vo.DataAnalysis;
import cn.xiaofangwei.onlinesurvey.service.*;
import cn.xiaofangwei.onlinesurvey.utils.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
    private final UserService userService;
    private final UserScoreService userScoreService;
    private final SubmitInfoService submitInfoService;
    private final QuestionService questionService;

    @Autowired
    public AnswerController(AnswerService answerService, QuestionnaireService questionnaireService, UserService userService, UserScoreService userScoreService, SubmitInfoService submitInfoService, QuestionService questionService) {
        this.answerService = answerService;
        this.questionnaireService = questionnaireService;
        this.userService = userService;
        this.userScoreService = userScoreService;
        this.submitInfoService = submitInfoService;
        this.questionService = questionService;
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
    public Message saveAnswer(@RequestBody AnswerVo answerVo, HttpServletRequest request, HttpSession session) throws SQLException {
        Integer questionnaireId = answerVo.getQuestionnaireId();
        Questionnaire questionnaire = questionnaireService.getById(questionnaireId);
        Integer statusType = questionnaire.getStatusType();
        if (statusType == 0) return Message.error("请先发布问卷！");
        if (questionnaire.getStopTime() != null && LocalDateTime.now().isAfter(questionnaire.getStopTime()))
            return Message.error("截止时间已过，已无法提交问卷！");
        QueryWrapper<Answer> wrapper = new QueryWrapper<>();
        wrapper.eq("questionnaireId", questionnaire.getQuestionnaireId());
        if (questionnaire.getDistribution() != 0 && questionnaire.getDistribution() <= answerService.count(wrapper))
            return Message.error("已超过问卷回收份数，无法提交问卷！");
        User user = (User) session.getAttribute("user");
        if (user != null) {
            user.setScore(user.getScore()+1);
            userService.updateById(user);
            UserScore us = new UserScore();
            us.setUserId(user.getUserId());
            us.setRecord("+1");
            us.setOptionTime(LocalDateTime.now());
            us.setDesc("回答问卷加1积分");
            userScoreService.save(us);
        }
        Integer useTime = answerVo.getUseTime();
        List<Question> questions = answerVo.getQuestions();
        String ip = request.getRemoteAddr();
        QueryWrapper<SubmitInfo> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("questionnaireId", answerVo.getQuestionnaireId());
        wrapper1.eq("submitIp", ip);
        if ( submitInfoService.getOne(wrapper1) != null ) return Message.error("您已回答过此问卷！");
        answerService.saveAnswers(questionnaireId, user.getUserId() == null ? 0 : user.getUserId(), questions, useTime, ip);
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

    @GetMapping("/analysisExportExcel")
    public void analysisExportExcel(@RequestParam("questionnaireId")Integer questionnaireId, HttpServletResponse response) throws Exception {
        Questionnaire questionnaire = questionnaireService.getById(questionnaireId);
        List<DataAnalysis> analysisList = answerService.getDataAnalysis(questionnaireId);

        String fileName = questionnaire.getTitle();
        String codedFileName = java.net.URLEncoder.encode(fileName, "UTF-8");;
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment;filename=" + codedFileName + ".xls");
        OutputStream out = response.getOutputStream();
        HSSFWorkbook wb = ExcelUtil.exportExcel(questionnaire, analysisList);

        wb.write(out);
        wb.close();
    }

    @GetMapping("/detail")
    public Message getAnswerDetail(@RequestParam("questionnaireId")Integer questionnaireId, @RequestParam("userId")Integer userId) throws SQLException {
        List<Question> questions = questionService.selectQuestionWithOptionsWithAnswer(questionnaireId, userId);
        return Message.info(questions);
    }
}
