package cn.xiaofangwei.onlinesurvey.mapper;

import cn.xiaofangwei.onlinesurvey.entity.Option;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */

public interface OptionMapper extends BaseMapper<Option> {

    List<Option> selectByQuestionId(Integer questionId);

}
