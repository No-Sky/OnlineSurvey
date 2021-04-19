package cn.xiaofangwei.onlinesurvey.controller;

import cn.xiaofangwei.onlinesurvey.entity.Message;
import cn.xiaofangwei.onlinesurvey.entity.Tag;
import cn.xiaofangwei.onlinesurvey.entity.UserTag;
import cn.xiaofangwei.onlinesurvey.service.QuestionnaireTagService;
import cn.xiaofangwei.onlinesurvey.service.TagService;
import cn.xiaofangwei.onlinesurvey.service.UserTagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagsController {

    private final UserTagService userTagService;

    private final QuestionnaireTagService questionnaireTagService;

    private final TagService tagService;

    public TagsController(UserTagService userTagService, QuestionnaireTagService questionnaireTagService, TagService tagService) {
        this.userTagService = userTagService;
        this.questionnaireTagService = questionnaireTagService;
        this.tagService = tagService;
    }

    @DeleteMapping("/qt")
    public Message deleteQT(@RequestParam("qtId") Integer qtId) throws SQLException {
        questionnaireTagService.removeById(qtId);
        return Message.info();
    }

    @GetMapping("/ut")
    public Message  getTagsByUser(@RequestParam("userId")Integer userId) throws SQLException{
        List<UserTag> userTags = userTagService.selectTagsByUser(userId);
        return Message.info(userTags);
    }

    @PostMapping("/ut")
    public Message addTagbyUser(UserTag userTag) throws SQLException {
        Tag newTag = new Tag();
//        System.out.println(userTag);
        if (userTag.getTagId() == null) {
            newTag.setName(userTag.getName());
            QueryWrapper<Tag> wrapper = new QueryWrapper<>();
            wrapper.eq("name", userTag.getName());
            Tag t = tagService.getOne(wrapper);
            if (t == null) {
                tagService.save(newTag);
            } else {
                newTag = t;
            }
        } else {
            newTag = tagService.getById(userTag.getTagId());
        }

        QueryWrapper<UserTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tagId", newTag.getTagId());
        UserTag userTag1 = userTagService.getOne(queryWrapper);
        if (userTag1 != null) {
            userTag1.setValue(userTag1.getValue()+1);
            userTagService.updateById(userTag1);
            return Message.info(2,"标签已存在");
        }
        userTag.setTagId(newTag.getTagId());
        userTagService.save(userTag);
        return Message.info();
    }

    @DeleteMapping("/ut")
    public Message deleteTagByUser(@RequestParam("utId") Integer utId) throws SQLException {
        userTagService.removeById(utId);
        return Message.info();
    }


}

