package cn.xiaofangwei.onlinesurvey.mapper;

import cn.xiaofangwei.onlinesurvey.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */

public interface UserMapper extends BaseMapper<User> {

    @Select("select userId, username, avatar from User where userId = #{userId}")
    User selectUserNameById(@Param("userId") Integer userId);

}
