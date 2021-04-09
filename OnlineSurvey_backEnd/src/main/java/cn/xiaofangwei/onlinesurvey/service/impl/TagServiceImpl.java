package cn.xiaofangwei.onlinesurvey.service.impl;

import cn.xiaofangwei.onlinesurvey.entity.Tag;
import cn.xiaofangwei.onlinesurvey.mapper.TagMapper;
import cn.xiaofangwei.onlinesurvey.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor=Exception.class)
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}
