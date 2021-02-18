package cn.xiaofangwei.onlinesurvey.utilstest;

import cn.xiaofangwei.onlinesurvey.exception.SendEmailException;
import cn.xiaofangwei.onlinesurvey.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailUtilsTest {

    @Autowired
    MailService mailService;

    @Test
    public void sendEmail() throws SendEmailException {
        mailService.sendSimpleMail("xfw.nosky@gmail.com", "OnlineSurvey验证码", "126354");
    }
}
