package cn.xiaofangwei.onlinesurvey.controller;

import cn.xiaofangwei.onlinesurvey.entity.Message;
import cn.xiaofangwei.onlinesurvey.entity.UserScore;
import cn.xiaofangwei.onlinesurvey.service.UserScoreService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/score")
public class UserScoreController {

    private final UserScoreService userScoreService;

    @Autowired
    public UserScoreController(UserScoreService userScoreService) {
        this.userScoreService = userScoreService;
    }

    @GetMapping("/list")
    public Message getUserScoreOption (@RequestParam("userId")Integer userId,
                                       @RequestParam("page")Integer page) throws SQLException {
        QueryWrapper<UserScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.orderByDesc("usId");
        Page<UserScore> userScorePage = new Page<>(page, 10);
        IPage<Map<String, Object>> mapIPage = userScoreService.selectMapsPage(userScorePage, queryWrapper);

        return Message.info("积分操作记录获取成功", mapIPage);
    }
}
