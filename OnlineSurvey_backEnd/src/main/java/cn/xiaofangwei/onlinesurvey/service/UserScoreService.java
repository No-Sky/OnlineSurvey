package cn.xiaofangwei.onlinesurvey.service;

import cn.xiaofangwei.onlinesurvey.entity.UserScore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface UserScoreService extends IService<UserScore> {
    IPage<Map<String, Object>> selectMapsPage(Page<UserScore> userScorePage, QueryWrapper<UserScore> queryWrapper);
}
