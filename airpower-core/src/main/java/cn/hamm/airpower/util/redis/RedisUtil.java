package cn.hamm.airpower.util.redis;

import cn.hamm.airpower.config.GlobalConfig;
import cn.hamm.airpower.root.RootEntity;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <h1>Redis封装类</h1>
 *
 * @author Hamm
 */
@SuppressWarnings("unused")
@Component
public class RedisUtil<E extends RootEntity<E>> {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private GlobalConfig globalConfig;

    /**
     * 从缓存中获取实体
     *
     * @param entity 实体
     * @return 实体
     */
    public final E getEntity(E entity) {
        Object object = get(getCacheKey(entity));
        if (Objects.isNull(object)) {
            return null;
        }
        String json = object.toString();
        if (Objects.isNull(json)) {
            return null;
        }
        //noinspection unchecked
        return (E) JSON.parseObject(json, entity.getClass());
    }

    /**
     * 删除指定的实体缓存
     *
     * @param entity 实体
     */
    public final void deleteEntity(E entity) {
        del(getCacheKey(entity));
    }

    /**
     * 缓存实体
     *
     * @param entity 实体
     */
    public final void saveEntity(E entity) {
        saveEntity(entity, globalConfig.getCacheExpireSecond());
    }

    /**
     * 缓存实体
     *
     * @param entity 实体
     * @param second 缓存时间(秒)
     */
    public final void saveEntity(E entity, long second) {
        set(getCacheKey(entity), JSON.toJSONString(entity), second);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key    键
     * @param second 时间(秒)
     */
    public final void setExpireSecond(String key, long second) {
        try {
            if (second > 0) {
                redisTemplate.expire(key, second, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            throw new RedisConnectionFailureException("Redis服务器连接失败");
        }
    }

    /**
     * 删除所有满足条件的数据
     *
     * @param pattern 正则
     */
    public final void clearAll(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (Objects.nonNull(keys)) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            throw new RedisConnectionFailureException("Redis服务器连接失败");
        }
    }

    /**
     * 获取过期时间
     *
     * @param key 缓存的Key
     * @return 过期时间
     */
    public final long getExpireSecond(String key) {
        try {
            //noinspection ConstantConditions
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RedisConnectionFailureException("Redis服务器连接失败");
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return <code>true</code> 存在; <code>false</code> 不存在
     */
    public final boolean hasKey(String key) {
        try {
            //noinspection ConstantConditions
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 键
     */
    public final void del(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new RedisConnectionFailureException("Redis服务器连接失败");
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public final Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            throw new RedisConnectionFailureException("Redis服务器连接失败");
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     */
    public final void set(String key, Object value) {
        set(key, value, globalConfig.getCacheExpireSecond());
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @apiNote time要大于0 如果time小于等于0 将设置无限期
     */
    public final void set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
        } catch (Exception e) {
            throw new RedisConnectionFailureException("Redis服务器连接失败");
        }
    }

    /**
     * 发布到channel的消息
     *
     * @param channel 频道
     * @param message 消息
     */
    public final void publish(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
    }

    /**
     * 获取缓存Entity的cacheKey
     *
     * @param entity 实体
     * @return key
     */
    private String getCacheKey(E entity) {
        return entity.getClass().getSimpleName() + "_" + entity.getId().toString();
    }
}