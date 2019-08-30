package xy.guard.service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import xy.guard.dao.dao.GuardNoticeGroupDao;
import xy.guard.dao.vo.GuardNoticeGroupVo;

/**
 * @author Ambitor
 */
@Service
public class GuardNoticeGroupService extends ServiceImpl<GuardNoticeGroupDao, GuardNoticeGroupVo> {

    /**
     * 获取组的告警对象
     */
    public List<GuardNoticeGroupVo> list(String groupCode) {
        LambdaQueryWrapper<GuardNoticeGroupVo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GuardNoticeGroupVo::getNoticeGroupCode, groupCode);
        return list(queryWrapper);
    }

}

