package cn.xiaofangwei.onlinesurvey.controller;

import cn.xiaofangwei.onlinesurvey.entity.Message;
import cn.xiaofangwei.onlinesurvey.entity.User;
import cn.xiaofangwei.onlinesurvey.exception.SendEmailException;
import cn.xiaofangwei.onlinesurvey.service.MailService;
import cn.xiaofangwei.onlinesurvey.service.UserService;
import cn.xiaofangwei.onlinesurvey.utils.GenerateVertifyCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @description 登录、注册、重置密码模块
 */

@RestController
public class BaseController {

    private final UserService userService;
    private final MailService mailService;

    @Autowired
    public BaseController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @PostMapping("/login")
    public Message login(@RequestParam("email") String email, @RequestParam("password")String password,
                          HttpSession session) {
        Map<String,Object> result = userService.login(email, password);
        if ( (Boolean) result.get("flag") ) {
            User user = (User) result.get("user");
            session.setAttribute("user", user);
            user.setPassword("");
            if (user.getUserId() == 1) {
                return Message.info(2, "欢迎超级管理员登录本系统！", user);
            }
            return Message.info("登录成功", user);
        }
        return Message.error((String) result.get("description"));
    }

    @GetMapping("/logincheck")
    public Message loginCheck(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            user.setPassword("");
            return Message.info(user);
        } else {
            return Message.error();
        }
    }

    @GetMapping("/logout")
    public Message logout(HttpSession session) {
        session.removeAttribute("user");
        return Message.info();
    }

    @PostMapping("/register")
    public Message register(User user, @RequestParam("vertifyCode")String vertifyCode,
                            HttpSession session) {
        String localVertifyCode = (String) session.getAttribute("vertifyCode");
        System.out.println(session.getId());
        if (!localVertifyCode.equals(vertifyCode)) {
            return Message.error("验证码输入错误，请重试!");
        }
        if (!userService.save(user)) {
            return Message.error("注册失败，请重试！");
        }
        return Message.info("注册成功，将自动跳转登录页！");
    }

    @PostMapping("/resetpass")
    public Message resetPassword(@RequestParam("email")String email, @RequestParam("vetifyCode")String vertifyCode,
                                 @RequestParam("newPassword")String newPassword, HttpSession session) {
        String localVertifyCode = (String) session.getAttribute("vertifyCode");
        if (!localVertifyCode.equals(vertifyCode)) {
            return Message.error("验证码输入错误，请重试!");
        }
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return Message.error("对不起，无此邮箱账号！");
        }
        user.setPassword(newPassword);
        if (!userService.updateById(user)) {
            return Message.error("操作失败，请重试！");
        }
        return Message.info("修改成功，将自动跳转登录页！");
    }

    /**
     * 发送验证码
     * @param email
     * @param session
     * @return
     */
    @GetMapping("/sendvertify")
    public Message sendVertifyCode(@RequestParam("email")String email, HttpSession session) throws SendEmailException {
        String code = GenerateVertifyCode.generateVerifyCode(6);
        mailService.sendEmail(email, code);
        session.setAttribute("vertifyCode", code);
        System.out.println(session.getId());
        removeAttrbute(session, "vertifyCode");
        return Message.info();
    }

    /**
     * 设置5分钟后删除session中的验证码
     * @param session
     * @param attrName
     */
    private void removeAttrbute(final HttpSession session, final String attrName) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 删除session中存的验证码
                session.removeAttribute(attrName);
                timer.cancel();
            }
        }, 5 * 60 * 1000);
    }

}
