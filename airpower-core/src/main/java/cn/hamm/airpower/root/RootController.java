package cn.hamm.airpower.root;

import cn.hamm.airpower.config.GlobalConfig;
import cn.hamm.airpower.interfaces.IAction;
import cn.hamm.airpower.interfaces.ITry;
import cn.hamm.airpower.enums.Result;
import cn.hamm.airpower.exception.ResultException;
import cn.hamm.airpower.model.json.Json;
import cn.hamm.airpower.model.json.JsonData;
import cn.hamm.airpower.annotation.Permission;
import cn.hamm.airpower.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>控制器根类</h1>
 *
 * @author Hamm.cn
 */
@Permission(login = false)
@RestController
@RequestMapping("")
@Slf4j
public class RootController implements IAction, ITry {
    /**
     * <h2>当前请求的实例</h2>
     */
    @Autowired
    protected HttpServletRequest request;

    /**
     * <h2>全局配置</h2>
     */
    @Autowired
    protected GlobalConfig globalConfig;

    /**
     * <h2>安全工具</h2>
     */
    @Autowired
    private SecurityUtil securityUtil;

    /**
     * <h2>响应一个JSON</h2>
     *
     * @param message 消息
     * @return JSON
     * @apiNote 如需抛出异常, 直接使用 {@link Result}
     */
    protected Json json(String message) {
        return new Json(message);
    }

    /**
     * <h2>响应一个JsonData</h2>
     *
     * @param data 数据
     * @return JsonData
     * @apiNote 如需抛出异常, 直接使用 {@link Result}
     */
    protected JsonData jsonData(Object data) {
        return new JsonData(data);
    }

    /**
     * <h2>响应一个JsonData</h2>
     *
     * @param data    数据
     * @param message 消息
     * @return JsonData
     * @apiNote 如需抛出异常, 直接使用 {@link Result}
     */
    protected JsonData jsonData(Object data, String message) {
        return new JsonData(data, message);
    }


    /**
     * <h2>获取当前登录用户的信息</h2>
     *
     * @return 用户ID
     */
    protected final long getCurrentUserId() {
        try {
            String accessToken = request.getHeader(globalConfig.getAuthorizeHeader());
            return securityUtil.getUserIdFromAccessToken(accessToken);
        } catch (Exception exception) {
            log.error("获取当前用户信息失败", exception);
            throw new ResultException(Result.UNAUTHORIZED);
        }
    }
}
