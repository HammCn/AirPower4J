package cn.hamm.airpower.open;

import cn.hamm.airpower.enums.ServiceError;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.model.Json;
import cn.hamm.airpower.util.Utils;

import java.util.Objects;

/**
 * <h1>开放接口的响应对象</h1>
 *
 * @author Hamm.cn
 */
public class OpenResponse {
    /**
     * <h2>加密响应数据</h2>
     *
     * @param openApp 应用
     * @param data    数据
     * @return 加密后的数据
     */
    public static <A extends IOpenApp> String encodeResponse(A openApp, Object data) {
        String response = null;
        if (Objects.isNull(data)) {
            // 数据负载为空 直接返回
            return response;
        }
        response = Json.toString(data);
        OpenArithmeticType appArithmeticType = Utils.getDictionaryUtil().getDictionary(
                OpenArithmeticType.class, openApp.getArithmetic()
        );
        try {
            switch (appArithmeticType) {
                case AES:
                    response = Utils.getAesUtil().setKey(openApp.getAppSecret()).encrypt(response);
                    break;
                case RSA:
                    response = Utils.getRsaUtil().setPrivateKey(openApp.getPrivateKey()).publicKeyEncrypt(response);
                    break;
                case NO:
                    break;
                default:
                    throw new ServiceException(ServiceError.ENCRYPT_DATA_FAIL, "不支持的加密算法");
            }
        } catch (Exception e) {
            ServiceError.ENCRYPT_DATA_FAIL.show();
        }
        return response;
    }
}
