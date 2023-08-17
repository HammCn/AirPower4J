package cn.hamm.airpower.websocket;

import cn.hamm.airpower.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

/**
 * WebSocket Handler
 *
 * @author hamm
 */
@Component
public class WebsocketHandler extends TextWebSocketHandler {


    @Autowired
    RedisTemplate redisTemplate;

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage textMessage) throws Exception {
        String message = textMessage.getPayload();
        if (WebsocketConfig.ping.equals(message)) {
            session.sendMessage(new TextMessage(WebsocketConfig.pong));
        }
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        try {
            String accessToken = session.getUri().getQuery();
            if(Objects.isNull(accessToken)){
                session.close();
                return;
            }
            Long userId = JwtUtil.getUserId(accessToken);
            redisTemplate.convertAndSend("USER_" + userId.toString(), "pong");
        } catch (Exception e) {
            session.close();
        }
    }
}
