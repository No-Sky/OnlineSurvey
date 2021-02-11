package cn.xiaofangwei.onlinesurvey.controller;

import cn.xiaofangwei.onlinesurvey.entity.Message;
import cn.xiaofangwei.onlinesurvey.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @description 登录、注册、重置密码模块
 */

@Controller
public class BaseController {

    @Autowired
    UserService userService;

    @RequestMapping("/login")
    @ResponseBody
    public Message login(@RequestParam("email") String email, @RequestParam("password")String password) {
        Map<String,Object> result = userService.login(email, password);
        if ( (Boolean) result.get("flag") ) {
            return Message.info("登录成功");
        }
        return Message.error((String) result.get("description"));
    }

}
