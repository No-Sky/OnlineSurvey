package cn.xiaofangwei.onlinesurvey.controller;


import cn.xiaofangwei.onlinesurvey.entity.*;
import cn.xiaofangwei.onlinesurvey.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
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

    private final TagService tagService;

    private final QuestionnaireTagService questionnaireTagService;

    private final UserService userService;

    private final UserScoreService userScoreService;

    @Autowired
    public QuestionnaireController(QuestionnaireService questionnaireService, TagService tagService, QuestionnaireTagService questionnaireTagService, UserService userService, UserScoreService userScoreService) {
        this.questionnaireService = questionnaireService;
        this.tagService = tagService;
        this.questionnaireTagService = questionnaireTagService;
        this.userService = userService;
        this.userScoreService = userScoreService;
    }

    @GetMapping
    public Message getQuestionnaireById(@RequestParam("questionnaireId")Integer questionnaireId) throws SQLException {
        Questionnaire questionnaire = questionnaireService.getById(questionnaireId);
        List<QuestionnaireTag> tags = questionnaireTagService.getTagsByQId(questionnaireId);
        System.out.println(tags);
        questionnaire.setTags(tags);
        return Message.info(questionnaire);
    }

    @GetMapping("/list")
    public Message getQuestionnaireList(@RequestParam("page")Integer page) throws SQLException{
        //分页条件
        QueryWrapper<Questionnaire> wrapper = new QueryWrapper<>();
        //分页序列，1：页面号，2：大小
        Page<Questionnaire> questionnairePage = new Page<>(page,10);
        IPage<Map<String, Object>> mapIPage = questionnaireService.selectMapsPage(questionnairePage, wrapper);
        return Message.info("页面获取成功", mapIPage);
    }

    @GetMapping("/user")
    public Message getQuestionnaireListByUser(@RequestParam("userId")Integer userId) throws SQLException {
        List<Questionnaire> questionnaireList = questionnaireService.selectQuestionnairesWithTagsByUser(userId);
        return Message.info(questionnaireList);
    }

    @GetMapping("/square")
    public Message getQuestionnaireByPublic(@RequestParam("page")Integer page) throws SQLException {
        //分页条件
        QueryWrapper<Questionnaire> wrapper = new QueryWrapper<>();
        wrapper.eq("isPublic", true).eq("statusType", 1);
        //分页序列，1：页面号，2：大小
        Page<Questionnaire> questionnairePage = new Page<>(page,5);
        IPage<Map<String, Object>> mapIPage = questionnaireService.selectMapsPage(questionnairePage, wrapper);
        return Message.info("页面获取成功", mapIPage);
    }


    @PostMapping
    public Message saveQuestionnaire(@RequestBody Questionnaire questionnaire) throws SQLException {
//        System.out.println(questionnaire);
        User user = userService.getById(questionnaire.getUserId());
        user.setScore(user.getScore()-2);
        if (user.getScore() < 0) return Message.error("积分不足无法创建问卷，请先获取积分");
        userService.updateById(user);
        questionnaire.setCreateTime(LocalDateTime.now());
        questionnaireService.saveOrUpdate(questionnaire);
        questionnaire.getTags().forEach(qt -> {
            Tag tag = new Tag();
            tag.setName(qt.getName());
            QueryWrapper<Tag> wrapper = new QueryWrapper<>();
            wrapper.eq("name", qt.getName());
            Tag t = tagService.getOne(wrapper);
            if (t == null) {
                tagService.saveOrUpdate(tag);
            } else {
                tag = t;
            }
            QuestionnaireTag questionnaireTag = new QuestionnaireTag();
            questionnaireTag.setQuestionnaireId(questionnaire.getQuestionnaireId());
            questionnaireTag.setTagId(tag.getTagId());
            questionnaireTagService.saveOrUpdate(questionnaireTag);

        });

        UserScore userScore = new UserScore();
        userScore.setUserId(user.getUserId());
        userScore.setRecord("-2");
        userScore.setOptionTime(LocalDateTime.now());
        userScore.setDesc("创建问卷扣除2积分");
        userScoreService.save(userScore);
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
