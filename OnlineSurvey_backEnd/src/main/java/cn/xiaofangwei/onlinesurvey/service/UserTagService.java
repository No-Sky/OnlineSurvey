package cn.xiaofangwei.onlinesurvey.service;


import cn.xiaofangwei.onlinesurvey.entity.UserTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserTagService extends IService<UserTag> {
    List<UserTag> selectTagsByUser(Integer userId);
}
