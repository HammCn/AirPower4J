package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.Permission;
import cn.hamm.airpower.config.AirConfig;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.config.MessageConstant;
import cn.hamm.airpower.enums.Error;
import cn.hamm.airpower.exception.SystemException;
import cn.hamm.airpower.interfaces.IAction;
import cn.hamm.airpower.interfaces.ITry;
import cn.hamm.airpower.util.AirUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>控制器根类</h1>
 *
 * @author Hamm.cn
 */
@Permission(login = false)
@RestController
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
            String accessToken = AirUtil.getRequest().getHeader(AirConfig.getGlobalConfig().getAuthorizeHeader());
            return AirUtil.getSecurityUtil().getUserIdFromAccessToken(accessToken);
        } catch (Exception exception) {
            log.error(MessageConstant.FAILED_TO_LOAD_CURRENT_USER_INFO, exception);
            throw new SystemException(Error.UNAUTHORIZED);
        }
    }
}
