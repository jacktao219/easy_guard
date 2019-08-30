package xy.guard.service.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * GuardServer阿波罗配置 Created by Ambitor on 2019/7/24
 *
 * @author Ambitor
 */
@Slf4j
@Data
@Component
public class GuardApolloConfig {

    @Value("${repeat.warn.time:30}")
    private Integer repeatWarnTime;
}
