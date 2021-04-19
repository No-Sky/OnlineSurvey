package cn.xiaofangwei.onlinesurvey.controller;


import cn.xiaofangwei.onlinesurvey.entity.Message;
import cn.xiaofangwei.onlinesurvey.entity.Questionnaire;
import cn.xiaofangwei.onlinesurvey.entity.SubmitInfo;
import cn.xiaofangwei.onlinesurvey.service.SubmitInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
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
@RequestMapping("/submit-info")
public class SubmitInfoController {

    private final SubmitInfoService submitInfoService;

    @Autowired
    public SubmitInfoController(SubmitInfoService submitInfoService) {
        this.submitInfoService = submitInfoService;
    }

    @GetMapping
    public Message getSubmitInfo(@RequestParam("submitInfoId") Integer submitInfoId) throws SQLException {
        SubmitInfo submitInfo = submitInfoService.getById(submitInfoId);
        return Message.info(submitInfo);
    }

    @GetMapping("/user")
    public Message getSubmitInfoListByUser(@RequestParam("userId")Integer userId, @RequestParam("page")Integer page) throws SQLException {
        QueryWrapper<SubmitInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        //分页序列，1：页面号，2：大小
        Page<SubmitInfo> submitInfoPage = new Page<>(page,10);
        IPage<Map<String, Object>> mapIPage = submitInfoService.selectMapsPage(submitInfoPage, queryWrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("pages", mapIPage.getPages());
        result.put("total", mapIPage.getTotal());
        result.put("records", mapIPage.getRecords());
        return Message.info("页面获取成功", result);
    }

    @GetMapping("/questionnaire")
    public Message getSubmitInfoListByQuestionnaire(@RequestParam("questionnaireId")Integer questionnaireId, @PathVariable("page")Integer page) throws SQLException {
        QueryWrapper<SubmitInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionnaireId", questionnaireId);
        //分页序列，1：页面号，2：大小
        Page<SubmitInfo> submitInfoPage = new Page<>(page,10);
        IPage<Map<String, Object>> mapIPage = submitInfoService.selectMapsPage(submitInfoPage, queryWrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("pages", mapIPage.getPages());
        result.put("total", mapIPage.getTotal());
        result.put("records", mapIPage.getRecords());
        return Message.info("页面获取成功", result);
    }

    @PostMapping
    public Message saveSubmitInfo(SubmitInfo submitInfo) throws SQLException {
        submitInfoService.save(submitInfo);
        return Message.info();
    }

    @PutMapping
    public Message updateSubmitInfo(SubmitInfo submitInfo) throws SQLException {
        submitInfoService.updateById(submitInfo);
        return Message.info();
    }

    @DeleteMapping
    public Message deleteSubmitInfo(@RequestParam("submitInfoId") Integer submitInfoId) throws SQLException {
        submitInfoService.removeById(submitInfoId);
        return Message.info();
    }

}
