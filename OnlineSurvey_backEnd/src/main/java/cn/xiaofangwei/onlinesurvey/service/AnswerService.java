package cn.xiaofangwei.onlinesurvey.service;

import cn.xiaofangwei.onlinesurvey.entity.Answer;
import cn.xiaofangwei.onlinesurvey.entity.Question;
import cn.xiaofangwei.onlinesurvey.entity.Questionnaire;
import cn.xiaofangwei.onlinesurvey.entity.vo.DataAnalysis;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */
public interface AnswerService extends IService<Answer> {

    void saveAnswers(Integer questionnaireId, List<Question> questions, Integer useTime, String ip) throws SQLException;

    List<DataAnalysis> getDataAnalysis(Integer questionnaireId);

    IPage<Map<String, Object>> selectMapsPage(Page<Answer> answerPage, QueryWrapper<Questionnaire> wrapper);
}
