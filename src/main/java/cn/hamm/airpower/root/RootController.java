package cn.hamm.airpower.root;

import cn.hamm.airpower.result.json.Json;
import cn.hamm.airpower.result.json.JsonData;
import cn.hamm.airpower.security.Permission;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>控制器根类</h1>
 *
 * @author hamm
 */
@Permission(login = false, authorize = false)
@RestController
@RequestMapping("")
public class RootController {
    /**
     * <h2>响应一个JSON</h2>
     *
     * @param message 消息
     * @return JSON
     */
    protected Json json(String message) {
        return new Json(message);
    }

    /**
     * <h2>响应一个JsonData</h2>
     *
     * @param data 数据
     * @return JsonData
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
     */
    protected JsonData jsonData(Object data, String message) {
        return new JsonData(data, message);
    }
}
