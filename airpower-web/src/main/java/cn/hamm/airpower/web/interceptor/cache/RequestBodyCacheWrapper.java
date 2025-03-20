package cn.hamm.airpower.web.interceptor.cache;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <h1>请求体缓存处理类</h1>
 *
 * @author Hamm.cn
 */
public class RequestBodyCacheWrapper extends HttpServletRequestWrapper {
    /**
     * <h3>缓存的请求体字节数组</h3>
     */
    private final byte[] cachedBody;

    /**
     * <h3>构造方法</h3>
     *
     * @param request 请求
     */
    public RequestBodyCacheWrapper(HttpServletRequest request) throws IOException {
        super(request);
        cachedBody = inputStreamToBytes(request.getInputStream());
    }

    /**
     * <h3>获取请求体输入流</h3>
     */
    @Contract(" -> new")
    @Override
    public final @NotNull ServletInputStream getInputStream() {
        return new CachedServletInputStream(new ByteArrayInputStream(cachedBody));
    }

    /**
     * <h3>获取请求阅读器</h3>
     */
    @Contract(" -> new")
    @Override
    public final @NotNull BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), UTF_8));
    }

    /**
     * <h3>将输入流转换为字节数组</h3>
     *
     * @param inputStream 输入流
     * @return 输入字节数组
     */
    private byte @NotNull [] inputStreamToBytes(@NotNull InputStream inputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        return out.toByteArray();
    }

    /**
     * <h3>缓存输入流</h3>
     */
    private static class CachedServletInputStream extends ServletInputStream {
        /**
         * <h3>输入流</h3>
         */
        private final ByteArrayInputStream inputStream;

        /**
         * <h3>构造方法</h3>
         *
         * @param inputStream 输入流
         */
        public CachedServletInputStream(ByteArrayInputStream inputStream) {
            this.inputStream = inputStream;
        }

        /**
         * <h3>读取</h3>
         */
        @Override
        public final int read() {
            return inputStream.read();
        }

        /**
         * <h3>是否结束</h3>
         */
        @Contract(pure = true)
        @Override
        public final boolean isFinished() {
            return false;
        }

        /**
         * <h3>是否就绪</h3>
         */
        @Contract(pure = true)
        @Override
        public final boolean isReady() {
            return true;
        }

        /**
         * <h3>设置读取监听器</h3>
         *
         * @param readListener 读取监听器
         */
        @Contract(pure = true)
        @Override
        public final void setReadListener(ReadListener readListener) {

        }
    }
}