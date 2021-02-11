package cn.xiaofangwei.onlinesurvey.service.impl;

import cn.xiaofangwei.onlinesurvey.entity.User;
import cn.xiaofangwei.onlinesurvey.mapper.UserMapper;
import cn.xiaofangwei.onlinesurvey.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */
@Service
class UserSericeImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public Map<String, Object> login(String email, String password) {
        Map<String, Object> result = new HashMap<>();
        User user = userMapper.selectByEmail(email);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                result.put("flag" ,true);
                result.put("user", user);
            } else {
                result.put("flag", false);
                result.put("description", "请重试，密码错误！");
            }
        } else {
            result.put("flag", false);
            result.put("description", "请重试，邮箱错误");
        }
        return result;
    }
}
