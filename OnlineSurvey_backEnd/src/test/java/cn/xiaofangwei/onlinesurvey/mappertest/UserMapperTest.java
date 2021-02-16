package cn.xiaofangwei.onlinesurvey.mappertest;

import cn.xiaofangwei.onlinesurvey.OnlinesurveyApplication;
import cn.xiaofangwei.onlinesurvey.entity.User;
import cn.xiaofangwei.onlinesurvey.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OnlinesurveyApplication.class)
public class UserMapperTest {
    @Resource
    private UserMapper userMapper;

    @Test
    public void test(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userMapper.selectList(queryWrapper);
    }
}
