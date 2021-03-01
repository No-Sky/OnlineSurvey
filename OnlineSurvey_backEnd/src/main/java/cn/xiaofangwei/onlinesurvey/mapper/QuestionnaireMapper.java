package cn.xiaofangwei.onlinesurvey.mapper;

import cn.xiaofangwei.onlinesurvey.entity.Questionnaire;
import cn.xiaofangwei.onlinesurvey.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */

public interface QuestionnaireMapper extends BaseMapper<Questionnaire> {

    @Select("select Questionnaire.* from Questionnaire")
    @Results(id = "QuestionnaireMap", value = {
            @Result(property = "userId", column = "userId"),
            @Result(property = "user", javaType = User.class, column = "userId",
                    one = @One(select = "cn.xiaofangwei.onlinesurvey.mapper.UserMapper.selectUserNameById"))
    })
    IPage<Questionnaire> selectPageVo(@Param("page") Page<Questionnaire> page);

}
