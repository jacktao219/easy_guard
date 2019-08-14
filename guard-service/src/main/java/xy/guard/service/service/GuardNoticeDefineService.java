package xy.guard.service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import xy.guard.dao.dao.GuardNoticeDefineDao;
import xy.guard.dao.vo.GuardNoticeDefineVo;

/**
 * GuardDefineService
 *
 * @author Ambitor
 */
@Service
public class GuardNoticeDefineService extends ServiceImpl<GuardNoticeDefineDao, GuardNoticeDefineVo> {

    /**
     * list
     */
    public List<GuardNoticeDefineVo> list(Integer guardDefineId) {
        LambdaQueryWrapper<GuardNoticeDefineVo> query = new LambdaQueryWrapper<>();
        query.eq(GuardNoticeDefineVo::getGuardDefineId, guardDefineId);
        return list(query);
    }
}



