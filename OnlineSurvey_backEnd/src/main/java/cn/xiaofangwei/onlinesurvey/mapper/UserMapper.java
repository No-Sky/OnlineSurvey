package cn.xiaofangwei.onlinesurvey.mapper;

import cn.xiaofangwei.onlinesurvey.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */

public interface UserMapper extends BaseMapper<User> {

    User selectByEmail(String email);

}
