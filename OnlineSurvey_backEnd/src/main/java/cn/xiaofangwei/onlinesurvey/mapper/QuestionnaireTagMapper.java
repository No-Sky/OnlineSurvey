package cn.xiaofangwei.onlinesurvey.mapper;

import cn.xiaofangwei.onlinesurvey.entity.QuestionnaireTag;
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
public interface QuestionnaireTagMapper extends BaseMapper<QuestionnaireTag> {

    @Select("select qtId, questionnaireId, qt.tagId, name from `Questionnaire_Tag` as qt " +
            "left join Tag as t on qt.tagId = t.tagId " +
            "where qt.questionnaireId = #{questionnaireId}")
    List<QuestionnaireTag> selectTagsByQuestionnaire(@Param("questionnaireId") Integer questionnaireId);
}
