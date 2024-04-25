package cn.hamm.airpower.util;

import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.enums.Result;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * <h1>请求助手类</h1>
 *
 * @author Hamm.cn
 */
@Slf4j
@Component
public class RequestUtil {
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    /**
     * <h2>多IP分割字符</h2>
     */
    private static final String MULTI_IP_ADDRESS_SPLITTER = ",";
    /**
     * <h2>IP地址字符串最大的长度</h2>
     */
    private static final int MAX_IP_ADDRESS_CHAR_LENGTH = 15;
    /**
     * <h2>错误信息</h2>
     */
    private static final String ERROR_MESSAGE = "你的IP地址异常";

    /**
     * <h2>判断是否是上传请求</h2>
     *
     * @param request 请求
     * @return 是否是上传请求
     */
    public final boolean isUploadRequest(@NotNull HttpServletRequest request) {
        return isUploadFileContentType(request.getContentType());
    }


    /**
     * <h2>判断是否是上传请求</h2>
     *
     * @param request 请求
     * @return 是否是上传请求
     */
    public final boolean isUploadRequest(@NotNull ServletRequest request) {
        return isUploadFileContentType(request.getContentType());
    }


    /**
     * <h2>获取IP地址</h2>
     *
     * @param request 请求
     * @return IP地址
     */
    public final String getIpAddress(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (isValidAddress(ipAddress)) {
                return ipAddress;
            }
            ipAddress = request.getHeader("Proxy-Client-IP");
            if (isValidAddress(ipAddress)) {
                return ipAddress;
            }
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
            if (isValidAddress(ipAddress)) {
                return ipAddress;
            }
            ipAddress = request.getRemoteAddr();
            if (Constant.LOCAL_IP_ADDRESS.equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                    if (isValidAddress(ipAddress)) {
                        return ipAddress;
                    }
                } catch (UnknownHostException exception) {
                    log.error("获取IP地址失败", exception);
                    Result.FORBIDDEN.show(ERROR_MESSAGE);
                }
            }
            return Constant.LOCAL_IP_ADDRESS;
        } catch (Exception exception) {
            log.error("获取IP地址失败", exception);
            Result.FORBIDDEN.show(ERROR_MESSAGE);
        }
        return "";
    }

    /**
     * <h2>判断是否上传文件的请求类型头</h2>
     *
     * @param contentType 请求类型头
     * @return 判断结果
     */
    @Contract(value = "null -> false", pure = true)
    private boolean isUploadFileContentType(String contentType) {
        return contentType != null && contentType.startsWith(MULTIPART_FORM_DATA);
    }

    /**
     * <h2>是否是有效的IP地址</h2>
     *
     * @param ipAddress IP地址
     * @return 判定结果
     */
    private boolean isValidAddress(String ipAddress) {
        return Objects.nonNull(ipAddress) &&
                !ipAddress.isEmpty() &&
                !Constant.LOCAL_IP_ADDRESS.equalsIgnoreCase(ipAddress);
    }

    /**
     * <h2>多IP获取真实IP地址</h2>
     *
     * @param ipAddress 原始IP地址
     * @return 处理之后的真实IP
     */
    private String getRealIpAddress(String ipAddress) {
        if (Objects.nonNull(ipAddress) &&
                ipAddress.length() > MAX_IP_ADDRESS_CHAR_LENGTH &&
                ipAddress.indexOf(MULTI_IP_ADDRESS_SPLITTER) > 0
        ) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }
        return ipAddress;
    }
}
