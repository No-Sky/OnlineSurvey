package cn.xiaofangwei.onlinesurvey.service;

import cn.xiaofangwei.onlinesurvey.entity.Questionnaire;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */
public interface QuestionnaireService extends IService<Questionnaire> {

    IPage<Map<String, Object>> selectMapsPage(Page<Questionnaire> questionnairePage, QueryWrapper<Questionnaire> wrapper);

    Questionnaire selectQuestionnaireWithQuestion(Integer questionnaireId);
}
