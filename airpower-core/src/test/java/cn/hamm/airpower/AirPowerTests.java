package cn.hamm.airpower;

import cn.hamm.airpower.model.Page;
import cn.hamm.airpower.util.HttpUtil;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;


public class AirPowerTests {
    @Test
    void init() {
        System.out.println("Hello AirPower Tests");
    }

    @Test
    void request() {
        var request = new HttpUtil();
        request.setUrl("https://api.hamm.cn/test/http");
        Page page = new Page();
//        HttpResponse<String> form = request.setContentType(ContentType.FORM_URLENCODED).post("a=1&b=2");
//        System.out.println(form.body());
//        HttpResponse<String> json = request.setContentType(ContentType.JSON).post(Json.toString(page));
//        System.out.println(json.body());
        Map<String, Object> cookies = new HashMap<>();
        cookies.put("a", "1");
        HttpResponse<String> cookie = request.setCookies(cookies).addHeader("who", "Hamm").send();
        System.out.println(cookie.body());
    }
}
