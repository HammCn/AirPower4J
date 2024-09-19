package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.ApiController;
import cn.hamm.airpower.annotation.Permission;
import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.enums.ServiceError;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.interfaces.IAction;
import cn.hamm.airpower.util.Utils;
import lombok.extern.slf4j.Slf4j;

/**
 * <h1>控制器根类</h1>
 *
 * @author Hamm.cn
 */
@Permission(login = false)
@ApiController(Constant.EMPTY_STRING)
@Slf4j
public class RootController implements IAction {
    /**
     * <h2>获取当前登录用户的信息</h2>
     *
     * @return 用户 {@code ID}
     */
    protected final long getCurrentUserId() {
        try {
            String accessToken = Utils.getRequest().getHeader(Configs.getServiceConfig().getAuthorizeHeader());
            return Utils.getSecurityUtil().getIdFromAccessToken(accessToken);
        } catch (Exception exception) {
            throw new ServiceException(ServiceError.UNAUTHORIZED);
        }
    }
}
