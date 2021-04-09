package cn.xiaofangwei.onlinesurvey.service.impl;

import cn.xiaofangwei.onlinesurvey.entity.UserTag;
import cn.xiaofangwei.onlinesurvey.mapper.UserTagMapper;
import cn.xiaofangwei.onlinesurvey.service.UserTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class UserTagServiceImpl extends ServiceImpl<UserTagMapper, UserTag> implements UserTagService {

    @Resource
    UserTagMapper userTagMapper;

    @Override
    public List<UserTag> selectTagsByUser(Integer userId) {
        return userTagMapper.selectTagsByUser(userId);
    }
}
