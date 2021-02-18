package cn.xiaofangwei.onlinesurvey.service.impl;

import cn.xiaofangwei.onlinesurvey.entity.Option;
import cn.xiaofangwei.onlinesurvey.mapper.OptionMapper;
import cn.xiaofangwei.onlinesurvey.service.OptionService;
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
public class OptionServiceImpl extends ServiceImpl<OptionMapper, Option> implements OptionService {

}
