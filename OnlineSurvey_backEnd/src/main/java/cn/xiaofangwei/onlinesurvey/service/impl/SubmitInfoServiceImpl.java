package cn.xiaofangwei.onlinesurvey.service.impl;

import cn.xiaofangwei.onlinesurvey.entity.SubmitInfo;
import cn.xiaofangwei.onlinesurvey.mapper.SubmitInfoMapper;
import cn.xiaofangwei.onlinesurvey.service.SubmitInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiaofangwei
 * @since 2021-02-06
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class SubmitInfoServiceImpl extends ServiceImpl<SubmitInfoMapper, SubmitInfo> implements SubmitInfoService {

    @Resource
    SubmitInfoMapper submitInfoMapper;

    @Override
    public IPage<Map<String, Object>> selectMapsPage(Page submitInfoPage, QueryWrapper queryWrapper) {
        return submitInfoMapper.selectPageVo(submitInfoPage, queryWrapper);
    }
}
