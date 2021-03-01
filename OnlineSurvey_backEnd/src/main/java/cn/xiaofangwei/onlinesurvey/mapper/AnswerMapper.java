package cn.xiaofangwei.onlinesurvey.mapper;

import cn.xiaofangwei.onlinesurvey.entity.Answer;
import cn.xiaofangwei.onlinesurvey.entity.vo.AnalysisSelectView;
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
 * @since 2021-02-06
 */

public interface AnswerMapper extends BaseMapper<Answer> {

    @Select("select A.optionId,count(B.submitId) as submitCount,A.content\n" +
            "        from (select * from `Option` where questionId = #{questionId}) A\n" +
            "         left join Answer B on A.optionId=B.answer\n" +
            "        group by A.optionId")
    List<AnalysisSelectView> selectOptionAnalysis(@Param("questionId") Integer questionId);
}
