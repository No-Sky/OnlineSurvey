package cn.xiaofangwei.onlinesurvey.exception;

import cn.xiaofangwei.onlinesurvey.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

@ControllerAdvice
@ResponseBody
public class WebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ExceptionHandler
    public Message unknownAccount(UnknownAccountException e) {
        log.error("账号不存在", e);
        return Message.error("账号不存在");
    }

    @ExceptionHandler
    public Message incorrectCredentials(IncorrectCredentialsException e) {
        log.error("密码错误", e);
        return Message.error("密码错误");
    }

    @ExceptionHandler(SQLException.class)
    public Message sqlException(SQLException e){
        log.error("数据库发生错误", e);
        return Message.error("对不起，请重试");
    }

    @ExceptionHandler
    public Message unknownException(Exception e) {
        log.error("发生了未知异常", e);
        // 发送邮件通知技术人员.
        return Message.error("系统出现错误, 请联系网站管理员!");
    }
}
