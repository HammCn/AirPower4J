package cn.hamm.airpower.util;

import cn.hamm.airpower.config.GlobalConfig;
import cn.hamm.airpower.enums.Result;
import cn.hamm.airpower.model.json.Json;
import cn.hamm.airpower.root.RootEntity;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <h1>Redis封装类</h1>
 *
 * @author Hamm.cn
 */
@Component
@Slf4j
public class RedisUtil {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private GlobalConfig globalConfig;

    /**
     * <h2>从缓存中获取实体</h2>
     *
     * @param entity 实体
     * @return 缓存实体
     */
    public final @Nullable <E extends RootEntity<E>> E getEntity(E entity) {
        Object object = get(getCacheKey(entity));
        if (Objects.isNull(object)) {
            return null;
        }
        String json = object.toString();
        if (Objects.isNull(json)) {
            return null;
        }
        //noinspection unchecked
        return (E) Json.parse(json, entity.getClass());
    }

    /**
     * <h2>从缓存中获取实体</h2>
     *
     * @param key    缓存的Key
     * @param entity 实体
     * @return 缓存的实体
     */
    public final @Nullable <E extends RootEntity<E>> E getEntity(String key, E entity) {
        Object object = get(key);
        if (Objects.isNull(object)) {
            return null;
        }
        String json = object.toString();
        if (Objects.isNull(json)) {
            return null;
        }
        //noinspection unchecked
        return (E) Json.parse(json, entity.getClass());
    }

    /**
     * <h2>删除指定的实体缓存</h2>
     *
     * @param entity 实体
     */
    public final <E extends RootEntity<E>> void deleteEntity(E entity) {
        del(getCacheKey(entity));
    }

    /**
     * <h2>缓存实体</h2>
     *
     * @param entity 实体
     */
    public final <E extends RootEntity<E>> void saveEntity(E entity) {
        saveEntity(entity, globalConfig.getCacheExpireSecond());
    }

    /**
     * <h2>缓存实体</h2>
     *
     * @param entity 实体
     * @param second 缓存时间(秒)
     */
    public final <E extends RootEntity<E>> void saveEntity(E entity, long second) {
        set(getCacheKey(entity), Json.toString(entity), second);
    }

    /**
     * <h2>缓存实体</h2>
     *
     * @param key    缓存的Key
     * @param entity 实体
     */
    public final <E extends RootEntity<E>> void saveEntity(String key, E entity) {
        saveEntity(key, entity, globalConfig.getCacheExpireSecond());
    }

    /**
     * <h2>缓存实体</h2>
     *
     * @param key    缓存的Key
     * @param entity 实体
     * @param second 缓存时间(秒)
     */
    public final <E extends RootEntity<E>> void saveEntity(String key, E entity, long second) {
        set(key, Json.toString(entity), second);
    }

    /**
     * <h2>指定缓存失效时间</h2>
     *
     * @param key    缓存的Key
     * @param second 缓存时间(秒)
     */
    public final void setExpireSecond(String key, long second) {
        try {
            if (second > 0) {
                redisTemplate.expire(key, second, TimeUnit.SECONDS);
            }
        } catch (Exception exception) {
            log.error("Redis服务器连接失败", exception);
            Result.REDIS_ERROR.show();
        }
    }

    /**
     * <h2>删除所有满足条件的数据</h2>
     *
     * @param pattern 正则
     */
    public final void clearAll(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (Objects.nonNull(keys)) {
                redisTemplate.delete(keys);
            }
        } catch (Exception exception) {
            log.error("Redis服务器连接失败", exception);
            Result.REDIS_ERROR.show();
        }
    }

    /**
     * <h2>获取过期时间</h2>
     *
     * @param key 缓存的Key
     * @return 过期时间
     */
    public final long getExpireSecond(String key) {
        try {
            //noinspection ConstantConditions
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception exception) {
            log.error("Redis服务器连接失败", exception);
            Result.REDIS_ERROR.show();
        }
        return 0;
    }

    /**
     * <h2>判断key是否存在</h2>
     *
     * @param key 缓存的Key
     * @return <code>true</code> 存在; <code>false</code> 不存在
     */
    public final boolean hasKey(String key) {
        try {
            //noinspection ConstantConditions
            return redisTemplate.hasKey(key);
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * <h2>删除缓存</h2>
     *
     * @param key 缓存的Key
     */
    public final void del(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception exception) {
            log.error("Redis服务器连接失败", exception);
            Result.REDIS_ERROR.show();
        }
    }

    /**
     * <h2>普通缓存获取</h2>
     *
     * @param key 缓存的Key
     * @return 值
     */
    public final @Nullable Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception exception) {
            log.error("Redis服务器连接失败", exception);
            Result.REDIS_ERROR.show();
        }
        return null;
    }

    /**
     * <h2>普通缓存放入</h2>
     *
     * @param key   键
     * @param value 值
     */
    public final void set(String key, Object value) {
        set(key, value, globalConfig.getCacheExpireSecond());
    }

    /**
     * <h2>普通缓存放入并设置时间</h2>
     *
     * @param key    缓存的Key
     * @param value  缓存的值
     * @param second 缓存时间(秒)
     * @apiNote time要大于0 如果time小于等于0 将设置无限期
     */
    public final void set(String key, Object value, long second) {
        try {
            if (second > 0) {
                redisTemplate.opsForValue().set(key, value, second, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
        } catch (Exception exception) {
            log.error("Redis服务器连接失败", exception);
            Result.REDIS_ERROR.show();
        }
    }

    /**
     * <h2>发布到channel的消息</h2>
     *
     * @param channel 频道
     * @param message 消息
     */
    public final void publish(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
    }

    /**
     * <h2>获取缓存Entity的cacheKey</h2>
     *
     * @param entity 实体
     * @return key
     */
    private @NotNull <E extends RootEntity<E>> String getCacheKey(@NotNull E entity) {
        return entity.getClass().getSimpleName() + "_" + entity.getId().toString();
    }
}