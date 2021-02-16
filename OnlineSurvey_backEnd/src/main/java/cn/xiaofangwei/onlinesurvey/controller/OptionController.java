package cn.xiaofangwei.onlinesurvey.controller;


import cn.xiaofangwei.onlinesurvey.entity.Message;
import cn.xiaofangwei.onlinesurvey.entity.Option;
import cn.xiaofangwei.onlinesurvey.service.OptionService;
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
@RequestMapping("/option")
public class OptionController {

    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    public Message getOptionByOptionId(@RequestParam("optionId")Integer optionId) throws SQLException {
        Option option = optionService.getById(optionId);
        return Message.info(option);
    }

    @GetMapping
    public Message getOptionByQuestionId(@RequestParam("questionId")Integer questionId) {
        QueryWrapper<Option> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionId", questionId);
        List<Object> options = optionService.listObjs(queryWrapper);
        return Message.info(options);
    }

    @PostMapping
    public Message saveOptioByQuestion(@RequestBody List<Option> options) throws SQLException {
        optionService.saveBatch(options);
        return Message.info();
    }

    @PutMapping
    public Message updateOption(Option option) throws SQLException {
        optionService.updateById(option);
        return Message.info();
    }

    @DeleteMapping
    public Message deleteOption(@RequestParam("optionId") Integer optionId) throws SQLException {
        optionService.removeById(optionId);
        return Message.info();
    }
}
