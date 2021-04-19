package cn.xiaofangwei.onlinesurvey.mapper;

import cn.xiaofangwei.onlinesurvey.entity.Questionnaire;
import cn.xiaofangwei.onlinesurvey.entity.SubmitInfo;
import cn.xiaofangwei.onlinesurvey.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */

public interface SubmitInfoMapper extends BaseMapper<SubmitInfo> {

    @Select("select * from SubmitInfo ${ew.customSqlSegment}")
    @Results(id = "SubmitInfoWithQuestionnaireMap", value = {
            @Result( property = "questionnaireId", column = "questionnaireId"),
            @Result(property = "questionnaire", javaType = Questionnaire.class, column = "questionnaireId",
                    one = @One(
                            select = "cn.xiaofangwei.onlinesurvey.mapper.QuestionnaireMapper.selectQuestionnaireWithTags"))
    })
    IPage<SubmitInfo> selectPageVo(@Param("page")Page<SubmitInfo> page, @Param(Constants.WRAPPER) QueryWrapper wrapper);

}
