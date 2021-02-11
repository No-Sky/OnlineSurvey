package cn.xiaofangwei.onlinesurvey.mapper;

import cn.xiaofangwei.onlinesurvey.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */
@Component
public interface UserMapper extends BaseMapper<User> {

    User selectByEmail(String email);

}
