package cn.hamm.airpower;

import cn.hamm.airpower.model.Json;
import cn.hamm.airpower.model.query.QueryPageResponse;
import cn.hamm.airpower.util.HttpUtil;
import org.junit.jupiter.api.Test;


public class AirPowerTests {
    @Test
    void init() {
        System.out.println("Hello AirPower Tests");
    }

    @Test
    void request() {
        var request = new HttpUtil();
        request.setUrl("https://api.hamm.cn/test/http?token=123");
        var body = new QueryPageResponse<>();
        var response = request
                .addHeader("key", "value")
                .addCookie("accessToken", "xqwdoiiwqo")
                .post(Json.toString(body));
        System.out.println(response.body());
    }
}