package cn.xiaofangwei.onlinesurvey.service;

import cn.xiaofangwei.onlinesurvey.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */
public interface UserService extends IService<User> {

    Map<String, Object> login(String email, String password);
}
