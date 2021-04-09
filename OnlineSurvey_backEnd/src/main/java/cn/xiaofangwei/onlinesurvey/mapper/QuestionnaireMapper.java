package cn.xiaofangwei.onlinesurvey.mapper;

import cn.xiaofangwei.onlinesurvey.entity.Questionnaire;
import cn.xiaofangwei.onlinesurvey.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */

public interface QuestionnaireMapper extends BaseMapper<Questionnaire> {

    @Select("select Questionnaire.* from Questionnaire ${ew.customSqlSegment}")
    @Results(id = "QuestionnaireWithUserMap", value = {
            @Result( property = "userId", column = "userId"),
            @Result(property = "user", javaType = User.class, column = "userId",
                    one = @One(
                            select = "cn.xiaofangwei.onlinesurvey.mapper.UserMapper.selectUserNameById")),
            @Result(property = "questionnaireId", column = "questionnaireId"),
            @Result(property = "tags", javaType = List.class, column = "questionnaireId",
                    many = @Many(
                            select = "cn.xiaofangwei.onlinesurvey.mapper.QuestionnaireTagMapper.selectTagsByQuestionnaire"))
    })
    IPage<Questionnaire> selectPageVo(@Param("page") Page<Questionnaire> page,  @Param(Constants.WRAPPER) QueryWrapper wrapper);

    @Select("select Questionnaire.* from Questionnaire where `userId` = #{userId}")
    @Results(id = "QuestionnaireWithTagsMap", value = {
            @Result(property = "questionnaireId", column = "questionnaireId"),
            @Result(property = "tags", javaType = List.class, column = "questionnaireId",
                    many = @Many(
                            select = "cn.xiaofangwei.onlinesurvey.mapper.QuestionnaireTagMapper.selectTagsByQuestionnaire"))
    })
    List<Questionnaire> selectQuestionnaireWithTagsByUser(@Param("userId") Integer userId);
}
