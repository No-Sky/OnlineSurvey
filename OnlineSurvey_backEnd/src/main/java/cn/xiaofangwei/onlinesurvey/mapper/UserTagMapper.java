package cn.xiaofangwei.onlinesurvey.mapper;

import cn.xiaofangwei.onlinesurvey.entity.User;
import cn.xiaofangwei.onlinesurvey.entity.UserTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-03-31
 */
public interface UserTagMapper extends BaseMapper<UserTag> {

    @Select("select utId, userId, ut.tagId, name, `value` from `User_Tag` as ut " +
            "left join `Tag` as t on ut.tagId = t.tagId  " +
            "where ut.userId = #{userId}")
    List<UserTag> selectTagsByUser(@Param("userId") Integer userId);
}
