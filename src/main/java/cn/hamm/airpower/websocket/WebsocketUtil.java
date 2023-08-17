package cn.hamm.airpower.websocket;

import cn.hamm.airpower.util.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>WebsocketUtil</h1>
 *
 * @author Hamm
 */
@Component
public class WebsocketUtil {
    @Autowired
    RedisUtil<?> redisUtil;

    /**
     * <h1>给所有人发消息</h1>
     *
     * @param message 消息内容
     */
    public void sendToAll(String message) {
        redisUtil.publish(WebsocketConfig.channelAll, message);
    }

    /**
     * <h1>给指定用户发消息</h1>
     *
     * @param userId  用户ID
     * @param message 消息内容
     */
    public void sendToUser(Long userId, String message) {
        redisUtil.publish(WebsocketConfig.channelUserPrefix + userId.toString(), message);
    }
}
