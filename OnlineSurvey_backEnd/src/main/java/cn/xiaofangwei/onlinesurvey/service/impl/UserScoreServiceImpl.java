package cn.xiaofangwei.onlinesurvey.service.impl;

import cn.xiaofangwei.onlinesurvey.entity.UserScore;
import cn.xiaofangwei.onlinesurvey.mapper.UserScoreMapper;
import cn.xiaofangwei.onlinesurvey.service.UserScoreService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service
@Transactional(rollbackFor=Exception.class)
public class UserScoreServiceImpl extends ServiceImpl<UserScoreMapper, UserScore> implements UserScoreService {

    @Resource
    UserScoreMapper userScoreMapper;

    @Override
    public IPage<Map<String, Object>> selectMapsPage(Page userScorePage, QueryWrapper queryWrapper) {
        return userScoreMapper.selectMapsPage(userScorePage, queryWrapper);
    }
}
