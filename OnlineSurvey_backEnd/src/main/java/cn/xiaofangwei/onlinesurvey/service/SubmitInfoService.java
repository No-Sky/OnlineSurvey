package cn.xiaofangwei.onlinesurvey.service;

import cn.xiaofangwei.onlinesurvey.entity.SubmitInfo;
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
public interface SubmitInfoService extends IService<SubmitInfo> {

    IPage<Map<String, Object>> selectMapsPage(Page submitInfoPage, QueryWrapper queryWrapper);
}
