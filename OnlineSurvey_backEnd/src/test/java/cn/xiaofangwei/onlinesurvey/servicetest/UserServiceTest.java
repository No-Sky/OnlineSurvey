package cn.xiaofangwei.onlinesurvey.servicetest;

import cn.xiaofangwei.onlinesurvey.OnlinesurveyApplication;
import cn.xiaofangwei.onlinesurvey.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OnlinesurveyApplication.class)
public class UserServiceTest {

    @Resource
    UserService userService;

    @Test
    public void findUserTest(){
        System.out.println(userService.list());
    }
}
