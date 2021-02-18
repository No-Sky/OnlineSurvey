package cn.xiaofangwei.onlinesurvey.service.impl;

import cn.xiaofangwei.onlinesurvey.entity.Answer;
import cn.xiaofangwei.onlinesurvey.mapper.AnswerMapper;
import cn.xiaofangwei.onlinesurvey.service.AnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */
@Service
@Transactional
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {

}
