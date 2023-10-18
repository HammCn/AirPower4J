package cn.hamm.airpower.websocket;

import cn.hamm.airpower.security.SecurityUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <h1>WebSocket Handler</h1>
 *
 * @author hamm
 */
@Component
public class WebsocketHandler extends TextWebSocketHandler implements MessageListener {

    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    private WebSocketSession currentSession;

    WebsocketHandler() {
    }

    WebsocketHandler(WebSocketSession session) {
        currentSession = session;
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage textMessage) throws Exception {
        String message = textMessage.getPayload();
        if (WebsocketConfig.ping.equals(message)) {
            session.sendMessage(new TextMessage(WebsocketConfig.pong));
        }
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        if (Objects.isNull(session.getUri())) {
            return;
        }
        try {
            String accessToken = session.getUri().getQuery();
            if (Objects.isNull(accessToken)) {
                session.close();
                return;
            }
            Long userId = securityUtil.getUserIdFromAccessToken(accessToken);
            redisTemplate.execute((connection) -> {
                connection.subscribe(new WebsocketHandler(session),
                        WebsocketConfig.channelAll.getBytes(StandardCharsets.UTF_8),
                        (WebsocketConfig.channelUserPrefix + userId.toString()).getBytes(StandardCharsets.UTF_8)
                );
                return null;
            }, false);
        } catch (Exception e) {
            e.printStackTrace();
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        session.close();
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        try {
            if (currentSession.isOpen()) {
                currentSession.sendMessage(new TextMessage(message.getBody()));
            }
        } catch (IOException ignored) {
        }
    }
}
