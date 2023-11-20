package cn.hamm.airpower.request;

import cn.hamm.airpower.result.Result;
import cn.hamm.airpower.result.ResultException;
import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * <h1>请求助手类</h1>
 *
 * @author Hamm
 * @noinspection unused
 */
public class RequestUtil {
    /**
     * <h2>本地地址</h2>
     */
    private static final String LOCAL_ADDRESS = "127.0.0.1";

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
     * <h2>获取IP地址</h2>
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
                    throw new ResultException(Result.FORBIDDEN, ERROR_MESSAGE);
                }
            }
            return LOCAL_ADDRESS;
        } catch (Exception e) {
            throw new ResultException(Result.FORBIDDEN, ERROR_MESSAGE);
        }
    }

    /**
     * <h2>是否是有效的IP地址</h2>
     *
     * @param ipAddress IP地址
     * @return 判定结果
     */
    private static boolean isValidAddress(String ipAddress) {
        return Objects.nonNull(ipAddress) && ipAddress.length() > 0 && !LOCAL_ADDRESS.equalsIgnoreCase(ipAddress);
    }

    /**
     * <h2>多IP获取真实IP地址</h2>
     *
     * @param ipAddress 原始IP地址
     * @return 处理之后的真实IP
     */
    private static String getRealIpAddress(String ipAddress) {
        if (Objects.nonNull(ipAddress) && ipAddress.length() > MAX_IP_ADDRESS_CHAR_LENGTH && ipAddress.indexOf(MULTI_IP_ADDRESS_SPLITTER) > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }
        return ipAddress;
    }
}
