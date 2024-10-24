package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.ApiController;
import cn.hamm.airpower.annotation.Permission;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.config.ServiceConfig;
import cn.hamm.airpower.exception.ServiceError;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.interfaces.IAction;
import cn.hamm.airpower.util.AccessTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <h1>控制器根类</h1>
 *
 * @author Hamm.cn
 */
@Permission(login = false)
@ApiController(Constant.EMPTY_STRING)
@Slf4j
public class RootController implements IAction {
    @Autowired
    protected ServiceConfig serviceConfig;

    @Autowired
    protected HttpServletRequest request;

    /**
     * <h2>获取当前登录用户的信息</h2>
     *
     * @return 用户 {@code ID}
     */
    protected final long getCurrentUserId() {
        try {
            String accessToken = request.getHeader(serviceConfig.getAuthorizeHeader());
            return AccessTokenUtil.create().getPayloadId(accessToken, serviceConfig.getAccessTokenSecret());
        } catch (Exception exception) {
            throw new ServiceException(ServiceError.UNAUTHORIZED);
        }
    }
}
