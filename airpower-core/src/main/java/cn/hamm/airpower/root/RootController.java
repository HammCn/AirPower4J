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
@Permission(login = false)
@RestController
@RequestMapping("")
public class RootController {
    /**
     * 响应一个JSON
     *
     * @param message 消息
     * @return JSON
     */
    protected Json json(String message) {
        return new Json(message);
    }

    /**
     * 响应一个JsonData
     *
     * @param data 数据
     * @return JsonData
     */
    protected JsonData jsonData(Object data) {
        return new JsonData(data);
    }

    /**
     * 响应一个JsonData
     *
     * @param data    数据
     * @param message 消息
     * @return JsonData
     */
    protected JsonData jsonData(Object data, String message) {
        return new JsonData(data, message);
    }
}
