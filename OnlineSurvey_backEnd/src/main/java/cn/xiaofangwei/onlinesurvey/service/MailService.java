package cn.xiaofangwei.onlinesurvey.service;


import cn.xiaofangwei.onlinesurvey.exception.SendEmailException;

public interface MailService {

    void sendSimpleMail(String to, String subject, String content) throws SendEmailException;

    void sendEmail(String email, String code) throws SendEmailException;
}
