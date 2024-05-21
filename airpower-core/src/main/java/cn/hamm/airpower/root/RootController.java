package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.Permission;
import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.config.MessageConstant;
import cn.hamm.airpower.enums.ServiceError;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.interfaces.IAction;
import cn.hamm.airpower.interfaces.ITry;
import cn.hamm.airpower.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <h1>控制器根类</h1>
 *
 * @author Hamm.cn
 */
@Permission(login = false)

@RequestMapping(Constant.EMPTY_STRING)
@Slf4j
public class RootController implements IAction, ITry {
    /**
     * <h2>获取当前登录用户的信息</h2>
     *
     * @return 用户ID
     */
    protected final long getCurrentUserId() {
        try {
            String accessToken = Utils.getRequest().getHeader(Configs.getServiceConfig().getAuthorizeHeader());
            return Utils.getSecurityUtil().getIdFromAccessToken(accessToken);
        } catch (Exception exception) {
            log.error(MessageConstant.FAILED_TO_LOAD_CURRENT_USER_INFO, exception);
            throw new ServiceException(ServiceError.UNAUTHORIZED);
        }
    }
}
