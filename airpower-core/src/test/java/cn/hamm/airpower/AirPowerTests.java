package cn.hamm.airpower;

import cn.hamm.airpower.util.NumberUtil;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;


public class AirPowerTests {
    @Test
    void init() {
        System.out.println("Hello AirPower Tests");
        NumberUtil numberUtil = new NumberUtil();
        System.out.println(numberUtil.add(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println(numberUtil.sub(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println(numberUtil.mul(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println(numberUtil.div(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println(numberUtil.round(1.23456789, 2, RoundingMode.UP));
        System.out.println(numberUtil.round(1.23456789, 2, RoundingMode.DOWN));
        System.out.println(numberUtil.round(1.23456789, 2, RoundingMode.HALF_EVEN));
        System.out.println(numberUtil.ceil(1.23456789, 2));
        System.out.println(numberUtil.floor(1.23456789, 2));
        System.out.println(numberUtil.add(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9));
        System.out.println(numberUtil.sub(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9));
        System.out.println(numberUtil.mul(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8));
        System.out.println(numberUtil.div(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8));
        System.out.println(0.1 + 0.2);
        System.out.println(numberUtil.add(0.1, 0.2));
        System.out.println(numberUtil.add(0.1, 0.1, 0.1));

    }
}
