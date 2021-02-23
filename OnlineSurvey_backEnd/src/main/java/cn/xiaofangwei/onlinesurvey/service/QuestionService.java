package cn.xiaofangwei.onlinesurvey.service;

import cn.xiaofangwei.onlinesurvey.entity.Question;
import cn.xiaofangwei.onlinesurvey.mapper.QuestionMapper;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */
public interface QuestionService extends IService<Question> {

    Question selectQuestionWithOptions(Integer questionId);

    List<Question> selectQuestionsWithOptions(Integer questionnaireId);

}
