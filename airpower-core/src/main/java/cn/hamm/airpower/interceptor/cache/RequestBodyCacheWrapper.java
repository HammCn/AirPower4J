package cn.hamm.airpower.interceptor.cache;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * <h1>请求体缓存处理类</h1>
 *
 * @author Hamm.cn
 */
public class RequestBodyCacheWrapper extends HttpServletRequestWrapper {
    /**
     * <h2>缓存的请求体字节数组</h2>
     */
    private final byte[] cachedBody;

    /**
     * <h2>构造方法</h2>
     *
     * @param request 请求
     */
    public RequestBodyCacheWrapper(HttpServletRequest request) throws IOException {
        super(request);
        cachedBody = inputStreamToBytes(request.getInputStream());
    }

    /**
     * <h2>获取请求体输入流</h2>
     */
    @Contract(" -> new")
    @Override
    public final @NotNull ServletInputStream getInputStream() {
        return new CachedServletInputStream(new ByteArrayInputStream(cachedBody));
    }

    /**
     * <h2>获取请求阅读器</h2>
     */
    @Contract(" -> new")
    @Override
    public final @NotNull BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    /**
     * <h2>将输入流转换为字节数组</h2>
     *
     * @param inputStream 输入流
     * @return 输入字节数组
     */
    private byte[] inputStreamToBytes(@NotNull InputStream inputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        return out.toByteArray();
    }

    /**
     * <h2>缓存输入流</h2>
     */
    private static class CachedServletInputStream extends ServletInputStream {
        /**
         * <h2>输入流</h2>
         */
        private final ByteArrayInputStream inputStream;

        /**
         * <h2>构造方法</h2>
         *
         * @param inputStream 输入流
         */
        public CachedServletInputStream(ByteArrayInputStream inputStream) {
            this.inputStream = inputStream;
        }

        /**
         * <h2>读取</h2>
         */
        @Override
        public final int read() {
            return inputStream.read();
        }

        /**
         * <h2>是否结束</h2>
         */
        @Contract(pure = true)
        @Override
        public final boolean isFinished() {
            return false;
        }

        /**
         * <h2>是否就绪</h2>
         */
        @Contract(pure = true)
        @Override
        public final boolean isReady() {
            return true;
        }

        /**
         * <h2>设置读取监听器</h2>
         *
         * @param readListener 读取监听器
         */
        @Contract(pure = true)
        @Override
        public final void setReadListener(ReadListener readListener) {

        }
    }
}