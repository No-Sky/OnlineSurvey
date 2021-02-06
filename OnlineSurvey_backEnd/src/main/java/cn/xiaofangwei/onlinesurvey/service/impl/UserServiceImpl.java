package cn.xiaofangwei.onlinesurvey.service.impl;

import cn.xiaofangwei.onlinesurvey.entity.User;
import cn.xiaofangwei.onlinesurvey.mapper.UserMapper;
import cn.xiaofangwei.onlinesurvey.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
