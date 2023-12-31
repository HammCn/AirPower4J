package cn.hamm.airpower.request;

import cn.hamm.airpower.result.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * <h1>请求助手类</h1>
 *
 * @author Hamm
 */
public class RequestUtil {
    /**
     * 本地地址
     */
    private static final String LOCAL_ADDRESS = "127.0.0.1";

    /**
     * 多IP分割字符
     */
    private static final String MULTI_IP_ADDRESS_SPLITTER = ",";

    /**
     * IP地址字符串最大的长度
     */
    private static final int MAX_IP_ADDRESS_CHAR_LENGTH = 15;

    /**
     * 错误信息
     */
    private static final String ERROR_MESSAGE = "你的IP地址异常";


    /**
     * 获取IP地址
     *
     * @param request 请求
     * @return IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
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
            if (LOCAL_ADDRESS.equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                    if (isValidAddress(ipAddress)) {
                        return ipAddress;
                    }
                } catch (UnknownHostException e) {
                    Result.FORBIDDEN.show(ERROR_MESSAGE);
                }
            }
            return LOCAL_ADDRESS;
        } catch (Exception e) {
            Result.FORBIDDEN.show(ERROR_MESSAGE);
        }
        return "";
    }

    /**
     * 是否是有效的IP地址
     *
     * @param ipAddress IP地址
     * @return 判定结果
     */
    private static boolean isValidAddress(String ipAddress) {
        return Objects.nonNull(ipAddress) && !ipAddress.isEmpty() && !LOCAL_ADDRESS.equalsIgnoreCase(ipAddress);
    }

    /**
     * 多IP获取真实IP地址
     *
     * @param ipAddress 原始IP地址
     * @return 处理之后的真实IP
     */
    @SuppressWarnings("unused")
    private static String getRealIpAddress(String ipAddress) {
        if (Objects.nonNull(ipAddress) && ipAddress.length() > MAX_IP_ADDRESS_CHAR_LENGTH && ipAddress.indexOf(MULTI_IP_ADDRESS_SPLITTER) > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }
        return ipAddress;
    }
}
