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

    private final byte[] cachedBody;

    public RequestBodyCacheWrapper(HttpServletRequest request) throws IOException {
        super(request);
        cachedBody = inputStreamToBytes(request.getInputStream());
    }

    @Contract(" -> new")
    @Override
    public final @NotNull ServletInputStream getInputStream() {
        return new CachedServletInputStream(new ByteArrayInputStream(cachedBody));
    }

    @Contract(" -> new")
    @Override
    public final @NotNull BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    private byte[] inputStreamToBytes(@NotNull InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        return out.toByteArray();
    }

    private static class CachedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream inputStream;

        public CachedServletInputStream(ByteArrayInputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public int read() {
            return inputStream.read();
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }
    }
}