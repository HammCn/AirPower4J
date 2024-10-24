package cn.hamm.airpower.util;

import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.exception.ServiceError;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.util.List;
import java.util.Objects;

/**
 * <h1>请求工具类</h1>
 *
 * @author Hamm.cn
 */
@Slf4j
public class RequestUtil {
    /**
     * <h2>获取IP地址异常</h2>
     */
    public static final String IP_ADDRESS_EXCEPTION = "获取IP地址异常";
    
    /**
     * <h2>常用IP反向代理Header头</h2>
     */
    private static final List<String> PROXY_IP_HEADERS = List.of(
            "x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP"
    );

    /**
     * <h2>禁止外部实例化</h2>
     */
    @Contract(pure = true)
    private RequestUtil() {
    }

    /**
     * <h2>判断是否是上传请求</h2>
     *
     * @param request 请求
     * @return 是否是上传请求
     */
    public static boolean isUploadRequest(@NotNull HttpServletRequest request) {
        return isUploadFileContentType(request.getContentType());
    }

    /**
     * <h2>判断是否是上传请求</h2>
     *
     * @param request 请求
     * @return 是否是上传请求
     */
    public static boolean isUploadRequest(@NotNull ServletRequest request) {
        return isUploadFileContentType(request.getContentType());
    }

    /**
     * <h2>获取请求的 {@code 真实IP} 地址</h2>
     *
     * @param request 请求
     * @return IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress;
        try {
            for (String ipHeader : PROXY_IP_HEADERS) {
                ipAddress = request.getHeader(ipHeader);
                if (Objects.equals(ipAddress, Constant.UNKNOWN)) {
                    continue;
                }
                if (isValidAddress(ipAddress)) {
                    return getIpAddressFromMultiIp(ipAddress);
                }
            }

            ipAddress = request.getRemoteAddr();
            if (!Objects.equals(Constant.LOCAL_IP_ADDRESS, ipAddress)) {
                return ipAddress;
            }
            // 根据网卡取本机配置的IP
            InetAddress inet;
            inet = InetAddress.getLocalHost();
            ipAddress = inet.getHostAddress();
            if (isValidAddress(ipAddress)) {
                return ipAddress;
            }
            return ipAddress;
        } catch (Exception exception) {
            ServiceError.FORBIDDEN.show(IP_ADDRESS_EXCEPTION);
        }
        return Constant.LOCAL_IP_ADDRESS;
    }

    /**
     * <h2>判断是否上传文件的请求类型头</h2>
     *
     * @param contentType 请求类型头
     * @return 判断结果
     */
    @Contract(value = "null -> false", pure = true)
    private static boolean isUploadFileContentType(String contentType) {
        return contentType != null && contentType.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE);
    }

    /**
     * <h2>是否是有效的IP地址</h2>
     *
     * @param ipAddress IP地址
     * @return 判定结果
     */
    private static boolean isValidAddress(String ipAddress) {
        return Objects.nonNull(ipAddress)
                && StringUtils.hasText(ipAddress)
                && !Constant.LOCAL_IP_ADDRESS.equalsIgnoreCase(ipAddress);
    }

    /**
     * <h2>多IP获取真实IP地址</h2>
     *
     * @param ipAddress 原始IP地址
     * @return 处理之后的真实IP
     */
    private static @NotNull String getIpAddressFromMultiIp(@NotNull String ipAddress) {
        if (ipAddress.indexOf(Constant.COMMA) > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(Constant.COMMA));
        }
        return ipAddress;
    }
}
