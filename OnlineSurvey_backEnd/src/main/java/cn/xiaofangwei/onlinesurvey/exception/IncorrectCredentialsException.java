package cn.xiaofangwei.onlinesurvey.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 密码错误异常类
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class IncorrectCredentialsException extends Exception{
    /**
     * 错误码
     */
    protected Integer errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;
}
