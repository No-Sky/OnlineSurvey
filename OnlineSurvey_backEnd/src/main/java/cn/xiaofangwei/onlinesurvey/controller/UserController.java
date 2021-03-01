package cn.xiaofangwei.onlinesurvey.controller;


import cn.xiaofangwei.onlinesurvey.entity.Message;
import cn.xiaofangwei.onlinesurvey.entity.User;
import cn.xiaofangwei.onlinesurvey.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@RequestMapping(("/user"))
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Message getUserById(@RequestParam("userId")Integer userId) throws SQLException {
        User user = userService.getById(userId);
        return Message.info(user);
    }

    @GetMapping("/list")
    public Message getUserList(@RequestParam("page")Integer page) throws SQLException {
        //分页条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //分页序列，1：页面号，2：大小
        Page<User> userPage = new Page<>(page,10);
        IPage<Map<String, Object>> mapIPage = userService.selectMapsPage(userPage, wrapper);
        return Message.info("页面获取成功", mapIPage);
    }

    @PostMapping
    public Message saveUser(User user) throws SQLException {
        userService.save(user);
        return Message.info();
    }

    @PutMapping
    public Message updateUserById(User user) throws SQLException {
        userService.updateById(user);
        return Message.info();
    }

    @DeleteMapping
    public Message deleteUserById(@RequestParam("userId")Integer userId) throws SQLException {
        userService.removeById(userId);
        return Message.info();
    }
}
